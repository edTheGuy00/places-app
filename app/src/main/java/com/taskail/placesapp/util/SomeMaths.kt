package com.taskail.placesapp.util

import android.location.Location
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.taskail.placesapp.data.models.Geometry
import io.reactivex.Single
import kotlin.math.roundToInt

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

fun getDistanceBetweenPoints(location: Location,
                             geometry: Geometry): String{


    return distance(location.latitude,
            geometry.location.lat,
            location.longitude,
            geometry.location.lng)

}

fun distance(lat1: Double, lat2: Double, lon1: Double,
             lon2: Double): String {

        val R = 6371 // Radius of the earth

        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)
        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + (Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2))
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        var distance = R.toDouble() * c * 1000.0 // convert to meters

        // don't care about height
        val height = 0.0 - 0.0

        distance = Math.pow(distance, 2.0) + Math.pow(height, 2.0)

        val dist = Math.sqrt(distance)

        return dist.roundToInt().toString() + "m"

}