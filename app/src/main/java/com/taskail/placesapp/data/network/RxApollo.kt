package com.taskail.placesapp.data.network

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.taskail.placesapp.AddNewPlaceMutation
import com.taskail.placesapp.CreateNewUserMutation
import com.taskail.placesapp.GetMyPlacesQuery
import com.taskail.placesapp.SearchNearbyQuery
import io.reactivex.Observable

/**
 *Created by ed on 4/15/18.
 */


fun createNewUser(phoneId: String): Observable<CreateNewUserMutation.Data> {

    return Observable.create { emitter ->
        newUserMutation(phoneId).enqueue(object : ApolloCall.Callback<CreateNewUserMutation.Data>(){
            override fun onFailure(e: ApolloException) {
                emitter.onError(e)
            }

            override fun onResponse(response: Response<CreateNewUserMutation.Data>) {
                val data = response.data()
                if (data != null)
                    emitter.onNext(data)
            }

        })
    }
}

fun saveNewFavorite(phoneId: String,
                    placeId: String,
                    lat: Double,
                    lng: Double,
                    name: String,
                    image: String?): Observable<AddNewPlaceMutation.Data> {

    return Observable.create { emitter ->
        addNewFavoritePlaceMuatation(phoneId,
                placeId,
                lat,
                lng,
                name,
                image)
                .enqueue(object : ApolloCall.Callback<AddNewPlaceMutation.Data>() {
                    override fun onFailure(e: ApolloException) {
                        emitter.onError(e)
                    }

                    override fun onResponse(response: Response<AddNewPlaceMutation.Data>) {
                        val data = response.data()
                        if (data != null) {
                            emitter.onNext(data)
                        }
                    }

                })
    }
}

fun getMyFavoritePlaces(phoneId: String): Observable<GetMyPlacesQuery.Data> {

    return Observable.create { emitter ->
        userPlacesQueryCall(phoneId)
                .enqueue(object : ApolloCall.Callback<GetMyPlacesQuery.Data>() {
                    override fun onFailure(e: ApolloException) {
                        emitter.onError(e)
                    }

                    override fun onResponse(response: Response<GetMyPlacesQuery.Data>) {

                        val data = response.data()
                        if (data != null) {
                            emitter.onNext(data)
                        }
                    }

                })
    }
}

fun searchPlacesNearby(location: String, radius: Int, key: String): Observable<SearchNearbyQuery.Data> {

    return Observable.create { emitter ->

        searchNearbyPlacesCall(location, radius, key)
                .enqueue(object : ApolloCall.Callback<SearchNearbyQuery.Data>() {
                    override fun onFailure(e: ApolloException) {
                        emitter.onError(e)
                    }

                    override fun onResponse(response: Response<SearchNearbyQuery.Data>) {
                        val data = response.data()
                        if (data != null) {
                            emitter.onNext(data)
                        }
                    }

                })
    }
}