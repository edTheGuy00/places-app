package com.taskail.placesapp.data.models

import com.google.gson.annotations.SerializedName



/**
 *Created by ed on 4/12/18.
 */

data class Photo(
        @SerializedName("height") val height: Int,
        @SerializedName("html_attributions") val htmlAttributions: List<Any>,
        @SerializedName("photo_reference") val photoReference: String,
        @SerializedName("width") val width: Int
)

