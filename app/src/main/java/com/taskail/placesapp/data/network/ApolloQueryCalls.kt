package com.taskail.placesapp.data.network

import com.apollographql.apollo.ApolloMutationCall
import com.apollographql.apollo.ApolloQueryCall
import com.taskail.placesapp.AddNewPlaceMutation
import com.taskail.placesapp.CreateNewUserMutation
import com.taskail.placesapp.GetMyPlacesQuery
import com.taskail.placesapp.SearchNearbyQuery

/**
 *Created by ed on 4/15/18.
 */



fun newUserMutation(phoneId: String): ApolloMutationCall<CreateNewUserMutation.Data> {
    return getApolloClient()
            .mutate(buildNewUserMutation(phoneId))
}

private fun buildNewUserMutation(phoneId: String): CreateNewUserMutation {
    return CreateNewUserMutation
            .builder()
            .phoneId(phoneId)
            .build()
}

fun addNewFavoritePlaceMuatation (phoneId: String,
                                  placeId: String,
                                  lat: Double,
                                  lng: Double,
                                  name: String,
                                  image: String?
): ApolloMutationCall<AddNewPlaceMutation.Data> {
    return getApolloClient()
            .mutate(buildAddFavPlacesMutation(phoneId, placeId, lat, lng, name, image))
}

private fun buildAddFavPlacesMutation(
        phoneId: String,
        placeId: String,
        lat: Double,
        lng: Double,
        name: String,
        image: String?
): AddNewPlaceMutation {

    return  AddNewPlaceMutation
            .builder()
            .phoneId(phoneId)
            .placeId(placeId)
            .lat(lat)
            .lng(lng)
            .name(name)
            .image(image)
            .build()
}

fun userPlacesQueryCall(phoneId: String): ApolloQueryCall<GetMyPlacesQuery.Data> {
    return getApolloClient()
            .query(buildUserPlacesQuery(phoneId))
}

private fun buildUserPlacesQuery(phoneId: String): GetMyPlacesQuery {
    return GetMyPlacesQuery
            .builder()
            .phoneId(phoneId)
            .build()
}

fun searchNearbyPlacesCall(location: String, radius: Int, key: String): ApolloQueryCall<SearchNearbyQuery.Data> {
    return getApolloClient()
            .query(buildSearchNearbyQuery(location, radius, key))
}

private fun buildSearchNearbyQuery(location: String, radius: Int, key: String): SearchNearbyQuery {
    return SearchNearbyQuery
            .builder()
            .location(location)
            .radius(radius)
            .key(key)
            .build()
}