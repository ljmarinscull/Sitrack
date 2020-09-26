package com.example.trackertest.ui

import android.Manifest
import android.content.*
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.trackertest.R
import com.example.trackertest.data.database.AppDatabase
import com.example.trackertest.data.repository.RecordRepoImpl
import com.example.trackertest.ui.service.LocationService
import com.example.trackertest.utils.isNotGPSActivated
import com.example.trackertest.utils.startActivityIntent
import com.example.trackertest.utils.toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener


class MainActivity : AppCompatActivity() {

    // The BroadcastReceiver used to listen from broadcasts from the service.
    private var myReceiver: MyReceiver? = null

    // A reference to the service used to get location updates.
    private var mService: LocationService? = null

    // Tracks the bound state of the service.
    private var mBound = false

    var mainViewModel : MainViewModel? = null

    // Monitors the state of the connection to the service.
    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as LocationService.RunServiceBinder
            mService = binder.getService()
            mBound = true
            mService?.foreground()
            mService?.requestLocationUpdates()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mService = null
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(
                this, MainViewModelFactory(
                    RecordRepoImpl(AppDatabase.getAppDataBase(applicationContext).recordDAO())
                )
            ).get(MainViewModel::class.java)

        myReceiver = MyReceiver()

        checkGPSPermission()

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_realtime, R.id.navigation_records
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


    override fun onStart() {
        super.onStart()

        bindService(
            Intent(this, LocationService::class.java), mServiceConnection, BIND_AUTO_CREATE)
    }

    override fun onResume() {
        super.onResume()
        if (myReceiver != null) {
            LocalBroadcastManager.getInstance(this).registerReceiver(
                myReceiver!!,
                IntentFilter(LocationService.ACTION_BROADCAST)
            )
        }

        mService?.background()
    }

    override fun onPause() {
        if (myReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver!!)
        }
        super.onPause()
    }

    override fun onStop() {
         if (mBound) {
            if (mService!!.isServiceRunning()) {
                mService!!.foreground()
            } else {
                stopService(Intent(this, LocationService::class.java))
            }

            unbindService(mServiceConnection)
            mBound = false
        }
        super.onStop()
    }


    inner class MyReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent) {
            val location: Location =
                intent.getParcelableExtra(LocationService.EXTRA_LOCATION)!!
            mainViewModel?.createRecord(location)
        }
    }

    private fun checkGPSPermission() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {

                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    if (isNotGPSActivated()) {
                        startActivityIntent<MainActivity>(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    toast("Porfavor conceda el permiso de Localizacion a al app.", 1)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }
}