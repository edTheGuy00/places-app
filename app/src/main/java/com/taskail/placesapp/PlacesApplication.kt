package com.taskail.placesapp

import android.app.Application

/**
 *Created by ed on 4/12/18.
 */

class PlacesApplication : Application() {

    companion object {
        var INSTANCE: PlacesApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}