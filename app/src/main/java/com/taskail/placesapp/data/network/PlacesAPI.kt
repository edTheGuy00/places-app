package com.taskail.placesapp.data.network

import com.taskail.placesapp.apiKey
import com.taskail.placesapp.data.models.Response
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *Created by ed on 4/12/18.
 *
 * source: https://developers.google.com/places/web-service/search#PlaceSearchRequests
 */

interface PlacesAPI {

    @GET("api/place/nearbysearch/json?&key=$apiKey")
    fun getNearbyPlaces(
            @Query("type") type: String,
            @Query("location") location: String,
            @Query("radius") radius: Int

    ) : Observable<Response>
}