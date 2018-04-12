package com.taskail.placesapp.util

import com.taskail.placesapp.PlacesApplication
import com.taskail.placesapp.R

/**
 *Created by ed on 4/12/18.
 */

fun nearbyString() : String {
    return PlacesApplication.INSTANCE.getString(R.string.places_nearby)
}

fun favoritesString() : String {
    return PlacesApplication.INSTANCE.getString(R.string.favorite_places)
}