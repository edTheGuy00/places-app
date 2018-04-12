package com.taskail.placesapp.main

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
    }
}