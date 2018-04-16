package com.taskail.placesapp.data

import com.taskail.placesapp.AddNewPlaceMutation
import com.taskail.placesapp.CreateNewUserMutation
import com.taskail.placesapp.GetMyPlacesQuery
import com.taskail.placesapp.SearchNearbyQuery
import com.taskail.placesapp.data.models.FavoritePlace

/**
 *Created by ed on 4/12/18.
 */

interface DataSource {

    fun getNearbyPlaces(location: String,
                        radius: Int,
                        apiKey: String,
                        handleResponse: (List<SearchNearbyQuery.SearchNearby>) -> Unit,
                        handleThrowable: (Throwable) -> Unit)

    fun getFavorites(phoneId: String,
                     handleFavorites: (List<GetMyPlacesQuery.Place>) -> Unit,
                     handleThrowable: (Throwable) -> Unit)

    fun saveFavorite(phoneId: String,
                     placeId: String,
                     lat: Double,
                     lng: Double,
                     name: String,
                     image: String?, handleOnSuccess: (AddNewPlaceMutation.Data) -> Unit)

    fun removeFavorite(favoritePlace: FavoritePlace)

    fun createNewUser(userId: String,
                      response: (CreateNewUserMutation.Data) -> Unit,
                      error: (Throwable) -> Unit)
}