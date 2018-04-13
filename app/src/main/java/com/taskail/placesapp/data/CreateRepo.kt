package com.taskail.placesapp.data

import com.taskail.placesapp.data.network.PlacesAPI
import com.taskail.placesapp.data.network.getRetrofitClient
import io.reactivex.disposables.CompositeDisposable

/**
 *Created by ed on 4/12/18.
 */

fun getRepository(compositeDisposable: CompositeDisposable) : PlacesRepository {
    return PlacesRepository(compositeDisposable, createPlacesApi())
}

private fun createPlacesApi() : PlacesAPI {
    return getRetrofitClient().create(PlacesAPI::class.java)
}