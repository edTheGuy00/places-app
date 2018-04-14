package com.taskail.placesapp.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.taskail.placesapp.data.models.FavoritePlace

/**
 *Created by ed on 4/13/18.
 */

/**
 * the database for this application.
 */
@Database(entities = [(FavoritePlace::class)], version = 1)
abstract class PlacesAppDatabase : RoomDatabase() {

    abstract fun favoritesDao(): FavoritesDao

    companion object {

        @Volatile private var databaseInstance: PlacesAppDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): PlacesAppDatabase {

            synchronized(lock) {
                if (databaseInstance == null) {
                    databaseInstance = Room.databaseBuilder(
                            context.applicationContext,
                            PlacesAppDatabase::class.java,
                            "placesApp.db")
                            .build()
                }
            }

            return databaseInstance!!
        }

    }
}