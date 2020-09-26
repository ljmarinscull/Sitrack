package com.example.trackertest.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Context.isNotGPSActivated() : Boolean {
    val manager =
        getSystemService(Context.LOCATION_SERVICE) as LocationManager?
   return !manager?.isProviderEnabled(LocationManager.GPS_PROVIDER)!!
}

inline fun <reified T : Activity> Activity.startActivityIntent(intent: Intent) {
    startActivity(intent)
}

fun ProgressBar.showProgress(){
    visibility = VISIBLE
    (this.context as AppCompatActivity).window.setFlags(
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
    )
}

fun ProgressBar.hideProgress(){
    visibility = GONE
    (this.context as AppCompatActivity).window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
}

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(this, msg, duration).show()
}

fun Context.toast(resId: Int, duration: Int = Toast.LENGTH_LONG){
    makeText(this, this.resources.getText(resId), duration).show()
}

fun Fragment.toast(msg: String, duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(requireContext(), msg, duration).show()
}

fun Context.getLocationText(location: Location?): String? {
    return if (location == null) "Unknown location" else "(" + location.latitude
        .toString() + ", " + location.longitude.toString() + ")"
}