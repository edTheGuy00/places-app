package com.taskail.placesapp.data.models

import com.google.gson.annotations.SerializedName



/**
 *Created by ed on 4/12/18.
 */

data class Result(
        @SerializedName("geometry") val geometry: Geometry,
        @SerializedName("icon") val icon: String,
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("opening_hours") val openingHours: OpeningHours,
        @SerializedName("photos") val photos: List<Photo>,
        @SerializedName("place_id") val placeId: String,
        @SerializedName("scope") val scope: String,
        @SerializedName("alt_ids") val altIds: List<AltId>,
        @SerializedName("reference") val reference: String,
        @SerializedName("types") val types: List<String>,
        @SerializedName("vicinity") val vicinity: String
)

