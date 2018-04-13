package com.taskail.placesapp.data

import com.taskail.placesapp.data.models.Response
import com.taskail.placesapp.data.network.PlacesAPI
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

/**
 *Created by ed on 4/12/18.
 */

class PlacesRepository(private val disposable: CompositeDisposable,
                       private val placesAPI: PlacesAPI) {

    private fun getPlaces(type: String, location: String, radius: Int) : Observable<Response> {
        return placesAPI.getNearbyPlaces(type, location, radius)
    }
}