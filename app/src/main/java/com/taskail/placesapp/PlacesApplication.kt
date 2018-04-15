package com.taskail.placesapp

import android.app.Application
import com.taskail.placesapp.data.local.PlacesAppDatabase
import com.taskail.placesapp.util.getUserPhoneId
import java.util.*

/**
 *Created by ed on 4/12/18.
 */

class PlacesApplication : Application() {

    lateinit var database: PlacesAppDatabase

    companion object {
        lateinit var INSTANCE: PlacesApplication
    }

    /**
     * create instance and database
     */
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        database = PlacesAppDatabase.getInstance(this)
    }
}