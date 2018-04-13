package com.taskail.placesapp.data.models

import com.google.gson.annotations.SerializedName

/**
 *Created by ed on 4/12/18.
 */

data class Response(
        @SerializedName("html_attributions") val htmlAttributions: List<Any>,
        @SerializedName("results") val results: List<Result>,
        @SerializedName("status") val status: String
)