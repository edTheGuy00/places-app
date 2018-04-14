package com.taskail.placesapp.data

import com.taskail.placesapp.data.local.FavoritesDao
import com.taskail.placesapp.data.local.getFavoritesFromDatabase
import com.taskail.placesapp.data.local.saveFavoriteToDatabase
import com.taskail.placesapp.data.models.FavoritePlace
import com.taskail.placesapp.data.models.Response
import com.taskail.placesapp.data.network.PlacesAPI
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

    /**
     * fetches all the nearby places from the google api
     * @param type the type of location we want
     * @param location the current location of the use
     * @param radius the radius around the users location
     * @param handleResponse a higher-order function that will be called when the results are returned
     * @param handleThrowable a higher-order function that will handle the errors
     */
    override fun getNearbyPlaces(type: String,
                                 location: String,
                                 radius: Int,
                                 handleResponse: (Response) -> Unit,
                                 handleThrowable: (Throwable) -> Unit) {

        fetchOnDisposable(getNearbyPlaces(type, location, radius), handleResponse, handleThrowable)

    }

    /**
     * get all the users favorite places from the local database
     * @param handleFavorites function that will be called to handle the list of favorites
     * @param handleThrowable function that will handle any errors
     */
    override fun getFavorites(handleFavorites: (List<FavoritePlace>) -> Unit,
                              handleThrowable: (Throwable) -> Unit) {
        fetchOnDisposable(getFavoritesFromDatabase(favoritesDao), handleFavorites, handleThrowable)
    }

    /**
     * builds
     * @property placesAPI.getNearbyPlaces
     * as an Observable
     */
    private fun getNearbyPlaces(type: String, location: String, radius: Int) : Observable<Response> {
        return placesAPI.getNearbyPlaces(type, location, radius)
    }

    /**
     * insert a new favorite place into the database
     * @param favoritePlace favorite to be inserted
     * @param handleOnSuccess function that will be called once item is save successfully
     */
    override fun saveFavorite(favoritePlace: FavoritePlace, handleOnSuccess: () -> Unit) {
        disposable.add(saveFavoriteToDatabase(favoritesDao, favoritePlace)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    handleOnSuccess()
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