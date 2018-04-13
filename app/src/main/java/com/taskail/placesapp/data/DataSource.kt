package com.taskail.placesapp.data

import com.taskail.placesapp.data.models.Response

/**
 *Created by ed on 4/12/18.
 */

interface DataSource {

    fun getNearbyPlaces(type: String,
                        location: String,
                        radius: Int,
                        response: (Response) -> Unit)
}