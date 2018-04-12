package com.taskail.placesapp.main

import com.google.android.gms.maps.model.LatLng

/**
 *Created by ed on 4/12/18.
 */

interface MainContract {

    interface MapView {

        var presenter: Presenter

        var isOpened: Boolean
    }

    interface Presenter {

        fun closeMapView()

        fun isLocationGranted(): Boolean

        fun requestLocation(zoomToLocation: (LatLng) -> Unit)
    }
}