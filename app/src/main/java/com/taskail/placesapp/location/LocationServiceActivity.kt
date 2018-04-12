package com.taskail.placesapp.location

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.location.LocationRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider

/**
 *Created by ed on 4/12/18.
 */

abstract class LocationServiceActivity : AppCompatActivity()  {

    private val TAG = javaClass.simpleName

    private val PERMISSION_REQUEST_FINE_LOCATION = 101

    private lateinit var locationProvider: ReactiveLocationProvider
    private lateinit var disposable: CompositeDisposable

    var permissionGranted = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (permissionGranted) {
            Log.d(TAG, "Checking location permissions")
            doubleCheckLocationPermission()
        }
    }

    /**
     * double check for permissions, pretty self-explanatory,
     * this will run every time the user opens the app.
     */
    private fun doubleCheckLocationPermission() {

        if (checkNeedsPermission()) {
            requestLocationPermission()
        } else {
            //Location is already granted
            locationGranted()
        }
    }

    /**
     * create the location provider, disposable and request for the last known location
     */
    private fun locationGranted() {
        locationProvider = ReactiveLocationProvider(this)
        disposable = CompositeDisposable()
        getLastKnownLocation()
    }

    /**
     * check for permissions
     */
    private fun checkNeedsPermission(): Boolean {
        return ContextCompat
                .checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
    }

    /**
     * request the location permission
     */
    private fun requestLocationPermission() {
        //requestLocationPermission: for devices higher than marshmallow, ask for permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_REQUEST_FINE_LOCATION)

        } else {
            //requestLocationPermission: for devices lower than marshmallow, location was already granted
            if (ContextCompat
                            .checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                //permission denied
                permissionGranted = false

            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when (requestCode) {
            PERMISSION_REQUEST_FINE_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    // permission was granted, yay! Do the
                    //  task you need to do.

                    locationGranted()

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    permissionGranted = false

                }
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }

    }

    /**
     * get the last known location,
     * we do not need to check for permissions since this will only be called if we already have permission
     */
    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation() {

        disposable.add(locationProvider.lastKnownLocation
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    lastKnowLocation(it)
                }, {

                }))
    }

    /**
     * get a more accurate location for the map,
     * we do not need to check for permissions since this will only be called if we already have permission
     */
    @SuppressLint("MissingPermission")
    fun getAccurateLocation(location: (Location) -> Unit) {

        val locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)
                .setNumUpdates(1)

        disposable.add(locationProvider
                .getUpdatedLocation(locationRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    location(it)
                }, {
                    Log.e(TAG, it.message)
                }))

    }

    override fun onDestroy() {
        super.onDestroy()

        if (permissionGranted) {
            disposable.clear()
        }
    }

    /**
     * pass on the last known location to main activity
     */
    abstract fun lastKnowLocation(location: Location)
}