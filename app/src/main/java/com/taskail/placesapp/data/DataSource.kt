package com.taskail.placesapp.data

import com.taskail.placesapp.CreateNewUserMutation
import com.taskail.placesapp.SearchNearbyQuery
import com.taskail.placesapp.data.models.FavoritePlace
import com.taskail.placesapp.data.models.Response

/**
 *Created by ed on 4/12/18.
 */

interface DataSource {

    fun getNearbyPlaces(location: String,
                        radius: Int,
                        apiKey: String,
                        handleResponse: (List<SearchNearbyQuery.SearchNearby>) -> Unit,
                        handleThrowable: (Throwable) -> Unit)

    fun getFavorites(handleFavorites: (List<FavoritePlace>) -> Unit,
                     handleThrowable: (Throwable) -> Unit)

    fun saveFavorite(favoritePlace: FavoritePlace, handleOnSuccess: () -> Unit)

    fun removeFavorite(favoritePlace: FavoritePlace)

    fun createNewUser(userId: String,
                      response: (CreateNewUserMutation.Data) -> Unit,
                      error: (Throwable) -> Unit)
}