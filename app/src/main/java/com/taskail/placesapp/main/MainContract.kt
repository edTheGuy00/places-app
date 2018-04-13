package com.taskail.placesapp.main

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.taskail.placesapp.data.models.Geometry
import com.taskail.placesapp.data.models.Result

/**
 *Created by ed on 4/12/18.
 */

interface MainContract {

    interface MapView {

        var presenter: Presenter

        var isOpened: Boolean

        fun zoomToLocation(myLocation: LatLng)

        fun addMarker(marker: MarkerOptions)
    }

    interface NearbyView {

        var presenter: Presenter

        fun displayResults(results: List<Result>)

        fun resultHasBeenLoaded() : Boolean
    }

    interface Presenter {

        fun handleSearchFabClick(latLngBounds: LatLngBounds)

        fun closeMapView()

        fun isLocationGranted(): Boolean

        fun requestLocation(zoomToLocation: (LatLng) -> Unit)

        fun fetchNearbyResults()

        fun calculateDistance(): (Geometry) -> String
    }
}