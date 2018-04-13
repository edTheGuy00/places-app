package com.taskail.placesapp.util

import android.location.Location
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import io.reactivex.Single

/**
 *Created by ed on 4/13/18.
 */

/**
 * an attempt at creating LatLngBounds based on a 10km radius of the users location
 */

fun calculateUserRadius(location: Location) : Single<LatLngBounds> {

    return Single.create<LatLngBounds> { e ->

        val lat = location.latitude
        val lng = location.longitude

        // Radius of 10km
        val searchRadius = 10000

        val longitudeD = Math.asin(searchRadius / (6378000 * Math.cos(Math.PI * lat / 180))) * 180 / Math.PI
        val latitudeD = Math.asin(searchRadius.toDouble() / 6378000.toDouble()) * 180 / Math.PI

        val latitudeMax = lat + latitudeD
        val latitudeMin = lat - latitudeD
        val longitudeMax = lng + longitudeD
        val longitudeMin = lng - longitudeD

        // not sure how to set SW NE bounds...?
        try {

            val southWest = LatLng(latitudeMax, longitudeMax)
            val northEast = LatLng(latitudeMin, longitudeMin)


            val latLngBounds = LatLngBounds(southWest, northEast)

            e.onSuccess(latLngBounds)
        } catch (e: Exception) {
            Log.e("calculate", e.message)
        }


    }
}