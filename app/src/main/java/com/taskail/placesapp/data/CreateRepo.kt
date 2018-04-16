package com.taskail.placesapp.data

import com.taskail.placesapp.data.local.FavoritesDao
import io.reactivex.disposables.CompositeDisposable

/**
 *Created by ed on 4/12/18.
 */

fun getRepository(compositeDisposable: CompositeDisposable, favoritesDao: FavoritesDao) : PlacesRepository {
    return PlacesRepository(compositeDisposable, favoritesDao)
}
