package com.taskail.placesapp.util

import com.google.android.gms.location.places.Place
import com.taskail.placesapp.data.models.FavoritePlace
import com.taskail.placesapp.data.models.Result

/**
 *Created by ed on 4/13/18.
 */


fun <T>createNewFavoritePlace(place: T): FavoritePlace {
    return when(place) {
        is Result -> {
            FavoritePlace(
                    place.id,
                    place.name,
                    place.types[0],
                    place.icon,
                    place.geometry.location.lat,
                    place.geometry.location.lng
            )
        } is Place -> {
            FavoritePlace(
                    place.id,
                    place.name.toString(),
                    "unknown",
                    "",
                    place.latLng.latitude,
                    place.latLng.longitude
            )
        }

        else -> throw IllegalArgumentException("Invalid Type")
    }
}