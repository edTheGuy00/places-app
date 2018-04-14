package com.taskail.placesapp.data.local

import com.taskail.placesapp.data.models.FavoritePlace
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.annotations.NonNull

/**
 *Created by ed on 4/13/18.
 */


@NonNull
fun saveFavoriteToDatabase(favoritesDao: FavoritesDao, favoritePlace: FavoritePlace): Completable{
    return Completable.create { e ->
        favoritesDao.addLocation(favoritePlace)
        e.onComplete()
    }
}

@NonNull fun removeFavFromDatabase(favoritesDao: FavoritesDao, favoritePlace: FavoritePlace): Completable {

    return Completable.create { e ->
        favoritesDao.removeFavorite(favoritePlace)
        e.onComplete()
    }
}