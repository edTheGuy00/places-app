package com.taskail.placesapp.data.network

import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 *Created by ed on 4/15/18.
 */

fun getApolloClient() : ApolloClient {

    val placesAppUrl = "https://places-app-graphql.herokuapp.com/"

    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

    val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    return ApolloClient.builder()
            .serverUrl(placesAppUrl)
            .okHttpClient(okHttpClient)
            .build()
}