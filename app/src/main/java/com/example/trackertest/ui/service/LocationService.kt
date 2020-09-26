package com.example.trackertest.ui.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.*
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.trackertest.R
import com.example.trackertest.ui.MainActivity
import com.google.android.gms.location.*

class LocationService: Service() {

    private val TAG: String = LocationService::class.java.simpleName

    private var isServiceRunning : Boolean = false
    private var mServiceHandler: Handler? = null

    private val serviceBinder: IBinder = RunServiceBinder()
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLocationCallback: LocationCallback? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocation: Location? = null

    companion object{
        const val PACKAGE_NAME =
            "com.example.trackertest.ui.locationservice"

        const  val ACTION_BROADCAST = "$PACKAGE_NAME.broadcast"

        const val EXTRA_LOCATION = "$PACKAGE_NAME.location"

        const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 30000

        /**
         * The identifier for the notification displayed for the foreground service.
         */
        const val NOTIFICATION_ID = 12345
    }

    override fun onBind(intent: Intent?): IBinder? {
        return serviceBinder
    }

    override fun onCreate() {
        super.onCreate()
        Log.v("Timer Service", "onCreate")

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                onNewLocation(locationResult.lastLocation)
            }
        }
        createLocationRequest()
        getLastLocation()
        val handlerThread = HandlerThread(TAG)
        handlerThread.start()
        mServiceHandler = Handler(handlerThread.looper)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) : Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("Service finish", "Finish")
        isServiceRunning = false
    }

    fun foreground() {
        startForeground(NOTIFICATION_ID, createNotification())
    }

    fun background() {
        stopForeground(true)
    }

    inner class RunServiceBinder : Binder() {
        fun getService() : LocationService {
            return this@LocationService
        }
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.fastestInterval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun getLastLocation() {
        try {
            mFusedLocationClient!!.lastLocation
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        mLocation = task.result
                    } else {
                        Log.w(TAG, "Failed to get location.")
                    }
                }
        } catch (unlikely: SecurityException) {
            Log.e(TAG, "Lost location permission.$unlikely")
        }
    }

    private fun onNewLocation(location: Location) {
        Log.i(TAG, "New location: $location")
        mLocation = location

        // Notify anyone listening for broadcasts about the new location.
        val intent = Intent(ACTION_BROADCAST)
        intent.putExtra(EXTRA_LOCATION, location)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    fun isServiceRunning() : Boolean {
        return isServiceRunning
    }

    fun requestLocationUpdates() {
        Log.i(TAG, "Requesting location updates")
        startService(Intent(applicationContext, LocationService::class.java))
        try {
            mFusedLocationClient!!.requestLocationUpdates(
                mLocationRequest,
                mLocationCallback, Looper.myLooper()
            )
        } catch (unlikely: SecurityException) {
            Log.e(TAG, "Lost location permission. Could not request updates. $unlikely")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String) : String {
        val chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = getColor(android.R.color.darker_gray)
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    private fun createNotification() : Notification {
        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel("my_service", "My Background Service")
        } else {
            ""
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Tracker Test")
            .setContentText("Regrese a la app")
            .setSmallIcon(R.mipmap.ic_launcher)

        val resultIntent = Intent(this, MainActivity::class.java)
        val resultPendingIntent =
            PendingIntent.getActivity(this, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(resultPendingIntent)

        return builder.build()
    }

}