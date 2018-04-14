package com.taskail.placesapp.data.local

import com.taskail.placesapp.data.models.FavoritePlace
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.annotations.NonNull

/**
 *Created by ed on 4/13/18.
 */

@NonNull
fun getFavoritesFromDatabase(@NonNull favoritesDao: FavoritesDao): Observable<List<FavoritePlace>> {

    return Observable.create { e ->

        val favorites = favoritesDao.getLocations()

        if (favorites.isNotEmpty()) {
            e.onNext(favorites)
        } else {
            e.onError(Throwable("Data Not Available"))
        }
    }
}

@NonNull
fun saveFavoriteToDatabase(favoritesDao: FavoritesDao, favoritePlace: FavoritePlace): Completable{
    return Completable.create { e ->
        favoritesDao.addLocation(favoritePlace)
        e.onComplete()
    }
}