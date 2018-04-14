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
 * receive the
 * @param place and determine what type it is then calculate the distance from
 * @param location and
 * @return distance in meters as a string
 */
fun <T> getDistanceBetweenPoints(location: Location,
                             place: T): String{


    return when(place){
        is Geometry -> {
            distance(location.latitude,
                    place.location.lat,
                    location.longitude,
                    place.location.lng)
        }
        is LatLng -> {
            distance(location.latitude,
                    place.latitude,
                    location.longitude,
                    place.longitude)
        }
        else -> throw IllegalArgumentException("Invalid Type")
    }



}

/**
 * calculate distance and
 * @return distance
 */
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