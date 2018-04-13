package com.taskail.placesapp.main

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

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

    interface Presenter {

        fun handleSearchFabClick()

        fun closeMapView()

        fun isLocationGranted(): Boolean

        fun requestLocation(zoomToLocation: (LatLng) -> Unit)
    }
}