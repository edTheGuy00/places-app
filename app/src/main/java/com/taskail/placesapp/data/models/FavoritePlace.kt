package com.taskail.placesapp.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 *Created by ed on 4/13/18.
 */

@Entity(tableName = "favorites")
data class FavoritePlace(
        @PrimaryKey var id: String,
        var name: String,
        var type: String,
        var icon: String,
        var lat: Double,
        var lng: Double
)