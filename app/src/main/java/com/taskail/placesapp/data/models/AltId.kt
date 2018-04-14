package com.taskail.placesapp.data.models
import com.google.gson.annotations.SerializedName


/**
 *Created by ed on 4/12/18.
 */

/**
 * src: https://developers.google.com/places/web-service/search#search_responses
 */
data class AltId(
		@SerializedName("place_id") val placeId: String,
		@SerializedName("scope") val scope: String
)

