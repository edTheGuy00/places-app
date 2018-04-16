package com.taskail.placesapp.data

import android.util.Log
import com.taskail.placesapp.AddNewPlaceMutation
import com.taskail.placesapp.CreateNewUserMutation
import com.taskail.placesapp.SearchNearbyQuery
import com.taskail.placesapp.data.local.FavoritesDao
import com.taskail.placesapp.data.local.removeFavFromDatabase
//import com.taskail.placesapp.data.local.getFavoritesFromDatabase
import com.taskail.placesapp.data.local.saveFavoriteToDatabase
import com.taskail.placesapp.data.models.FavoritePlace
import com.taskail.placesapp.data.models.Response
import com.taskail.placesapp.data.network.PlacesAPI
import com.taskail.placesapp.data.network.createNewUserMutation
import com.taskail.placesapp.data.network.saveNewFavorite
import com.taskail.placesapp.data.network.searchPlacesNearby
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 *Created by ed on 4/12/18.
 */

class PlacesRepository(private val disposable: CompositeDisposable,
                       private val placesAPI: PlacesAPI,
                       private val favoritesDao: FavoritesDao) :
        DataSource {

    private val TAG = javaClass.simpleName

    /**
     * fetches all the nearby places from the google api
     * @param type the type of location we want
     * @param location the current location of the user
     * @param radius the radius around the users location
     * @param handleResponse a higher-order function that will be called when the results are returned
     * @param handleThrowable a higher-order function that will handle the errors
     */
    override fun getNearbyPlaces(location: String,
                                 radius: Int,
                                 apiKey: String,
                                 handleResponse: (List<SearchNearbyQuery.SearchNearby>) -> Unit,
                                 handleThrowable: (Throwable) -> Unit) {

        disposable.add(searchPlacesNearby( location, radius, apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val list = it.searchNearby()
                    handleResponse(list)
                }, {

                }))

    }

    /**
     * get all the users favorite places from the local database
     * @param handleFavorites function that will be called to handle the list of favorites
     * @param handleThrowable function that will handle any errors,
     *
     * here we subscribe on a flowable to publish results when the database changes.
     */
    override fun getFavorites(handleFavorites: (List<FavoritePlace>) -> Unit,
                              handleThrowable: (Throwable) -> Unit) {

        disposable.add(favoritesDao.getLocations()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    handleFavorites(it)
                }, {
                    handleThrowable(it)
                }))

        //fetchOnDisposable(getFavoritesFromDatabase(favoritesDao), handleFavorites, handleThrowable)
    }

    override fun removeFavorite(favoritePlace: FavoritePlace) {
        disposable.add(removeFavFromDatabase(favoritesDao, favoritePlace)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d(TAG, "removed successfully")
                }))
    }

    override fun createNewUser(userId: String,
                               response: (CreateNewUserMutation.Data) -> Unit,
                               error: (Throwable) -> Unit) {

        fetchOnDisposable(createNewUserMutation(userId), response, error)
    }

    /**
     * builds
     * @property placesAPI.getNearbyPlaces
     * as an Observable
     */
    private fun getNearbyPlaces(type: String, location: String, radius: Int, apiKey: String) : Observable<Response> {
        return placesAPI.getNearbyPlaces(type, location, radius, apiKey)
    }

    /**
     * insert a new favorite place into the database
     * @param favoritePlace favorite to be inserted
     * @param handleOnSuccess function that will be called once item is saved successfully
     */
//    override fun saveFavorite(favoritePlace: FavoritePlace, handleOnSuccess: () -> Unit) {
//        disposable.add(saveFavoriteToDatabase(favoritesDao, favoritePlace)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    handleOnSuccess()
//                }))
//    }

    override fun saveFavorite(phoneId: String, placeId: String, lat: Double, lng: Double, name: String, image: String?, handleOnSuccess: (AddNewPlaceMutation.Data) -> Unit) {
        disposable.add(saveNewFavorite(phoneId, placeId, lat, lng, name, image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    handleOnSuccess(it)
                }, {
                    Log.d(TAG, it.message)
                }))
    }

    /**
     * flexible way of passing different observables and functions to the disposable
     */
    private fun <T> fetchOnDisposable(observable: Observable<T>,
                                      function: (T) -> Unit,
                                      throwable: (Throwable) -> Unit) {

        disposable.add(observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    function.invoke(it)
                }, {
                    throwable.invoke(it)
                }))

    }
}