package com.taskail.placesapp.data

import com.taskail.placesapp.data.models.Response
import com.taskail.placesapp.data.network.PlacesAPI
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 *Created by ed on 4/12/18.
 */

class PlacesRepository(private val disposable: CompositeDisposable,
                       private val placesAPI: PlacesAPI) :
        DataSource {

    override fun getNearbyPlaces(type: String,
                                 location: String,
                                 radius: Int,
                                 response: (Response) -> Unit) {

        fetchOnDisposable(getNearbyPlaces(type, location, radius), response)

    }

    private fun getNearbyPlaces(type: String, location: String, radius: Int) : Observable<Response> {
        return placesAPI.getNearbyPlaces(type, location, radius)
    }

    private fun <T> fetchOnDisposable(observable: Observable<T>,
                                      function: (T) -> Unit) {

        disposable.add(observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    function(it)
                }, {

                }))

    }
}