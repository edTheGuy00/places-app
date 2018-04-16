package com.taskail.placesapp.main

import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.Place
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.taskail.googleplacessearchdialog.SimplePlacesSearchDialog
import com.taskail.googleplacessearchdialog.SimplePlacesSearchDialogBuilder
import com.taskail.placesapp.PlacesApplication
import com.taskail.placesapp.R
import com.taskail.placesapp.SearchNearbyQuery
import com.taskail.placesapp.data.DataSource
import com.taskail.placesapp.data.getRepository
import com.taskail.placesapp.data.models.FavoritePlace
import com.taskail.placesapp.location.LocationServiceActivity
import com.taskail.placesapp.ui.PlaceBottomSheetView
import com.taskail.placesapp.ui.TabsPagerAdapter
import com.taskail.placesapp.ui.animation.DismissibleAnimation
import com.taskail.placesapp.util.*
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_viewpage_list.*
import java.util.*

/**
 * the main entry point and the presenter for the application.
 */

class MainActivity : LocationServiceActivity(), MainContract.Presenter {

    private val TAG = javaClass.simpleName

    private val database by lazy { (application as PlacesApplication).database }

    private lateinit var pagerAdapter: TabsPagerAdapter
    private var mapView: MainContract.MapView? = null
    private var currentLocation: Location? = null

    private var userId: String? = null

    private lateinit var repository: DataSource
    private lateinit var disposable: CompositeDisposable
    private lateinit var nearbyView: NearbyFragment
    private lateinit var favoritesView: FavoritesFragment

    private var locationReceived = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        disposable = CompositeDisposable()
        repository = getRepository(disposable, database.favoritesDao())

        pagerAdapter = TabsPagerAdapter(supportFragmentManager)
        setupViewPager()

        fab.setOnClickListener {
            openMapsViewFragment()
        }

        val phoneId = getUserPhoneId()
        if (phoneId == "none") {
            Log.d(TAG, "null $phoneId")
            createAndSaveNewUser()
        } else {
            Log.d(TAG, "not null $phoneId")
            userId = phoneId
        }
    }

    private fun createAndSaveNewUser() {

        val newId = UUID.randomUUID().toString()

        Log.d(TAG, "creating new user $newId")
        repository.createNewUser(newId, {
            Log.d(TAG, it.newUser())
            saveUserPhoneId(newId)
            userId = newId
        }, {
            Log.e(TAG, it.message)
        })
    }
    /**
     * get all establishment within a 5km radius of the users
     * current location.
     */
    override fun fetchNearbyResults() {
        Log.d(TAG, "request results")
        if (locationReceived) {
            repository.getNearbyPlaces(
                    location = "${currentLocation?.latitude},${currentLocation?.longitude}",
                    radius = 5000,
                    apiKey = getString(R.string.google_api_key),
                    handleResponse = {

                        nearbyView.displayResults(it)

                    },
                    handleThrowable ={
                        Log.e(TAG, "something went wrong")
                    })
        } else {
            Log.d(TAG, "no location")
        }
    }

    /**
     * get all of the users favorite places from the local database
     */
    override fun getFavoritePlaces() {
        repository.getFavorites(userId!!,
                {
            favoritesView.displayFavorites(it)
        }, {
            Log.d(TAG, "Something went wrong")
        })
    }

    /**
     * this function will take
     * @param T as either
     * @property Geometry or
     * @property LatLng and it will calculate the distance from the user and
     * @return a distance in meters as a string
     */
    override fun <T> calculateDistance(): (T) -> String {
        return {getDistanceBetweenPoints(
                currentLocation!!,
                it)}
    }

    override fun lastKnowLocation(location: Location) {
        Log.d(TAG, "location received")
        currentLocation = location
        locationReceived = true

        if (!nearbyView.resultHasBeenLoaded()) {
            fetchNearbyResults()
        }
    }

    override fun requestLocation(zoomToLocation: (LatLng) -> Unit) {
        getAccurateLocation {
            currentLocation = it
            val myLatLng = LatLng(it.latitude, it.longitude)
            zoomToLocation.invoke(myLatLng)
        }
    }

    /**
     *
     */
    override fun isLocationGranted(): Boolean {
        return permissionGranted
    }

    /**
     * Load the two main fragments into the viewpager
     */
    private fun setupViewPager() {

        pagerAdapter.addFragment(NearbyFragment().apply {
            Log.d(TAG, "adding fragment to view pager")
            nearbyView = this
            presenter = this@MainActivity
        }, nearbyString())
        pagerAdapter.addFragment(FavoritesFragment().apply {
            favoritesView = this
            presenter = this@MainActivity
        }, favoritesString())

        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    /**
     * Clicking on an item fro either the favorites fragment or
     * results fragment will open up a BottomSheet Fragment. we pass
     * @param place to display.
     */
    override fun <T> openBottomSheet(place: T) {
        val placeBottomSheet = PlaceBottomSheetView().apply {
            presenter = this@MainActivity
            when(place) {
                is SearchNearbyQuery.SearchNearby -> this.place = place
                is FavoritePlace -> this.place = place
            }
        }

        placeBottomSheet.show(supportFragmentManager, placeBottomSheet.tag)
    }

    /**
     * will be called from the BottomSheet to open the MapView
     */
    override fun viewLocationOnMap(location: LatLng, name: String) {
        Log.d(TAG, location.toString())
        val marker = MarkerOptions().position(location).title(name)
        openMapsViewFragment(location, marker)
    }

    /**
     * Open the url link that was obtained from the places search.
     */
    override fun opnUrl(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        if (intent.resolveActivity(this.packageManager) != null) {
            startActivity(intent)
        }else{
            Log.e(TAG,"No web browser installed")
        }
    }

    /**
     * save a new favorite place,
     * @param placeToFavorite the generic type of place to be saved.
     */
    override fun <T> saveToFavorites(placeToFavorite: T) {
        val place = placeToFavorite as SearchNearbyQuery.SearchNearby
        repository.saveFavorite(userId!!,
                place.place_id(),
                place.geometry().location().lat()!!,
                place.geometry().location().lng()!!,
                place.name(),
                place.icon(),
                {
                    Log.d(TAG, "saved successfully")
                })
    }

    override fun deleteFavorite(favoritePlace: FavoritePlace) {
        repository.removeFavorite(favoritePlace)
    }

    /**
     * Map view can be opened from 3 different places,
     * results from google api,
     * favorited items,
     * or the fab.
     * @param location and
     * @param marker will only be passed from results or favorite fragments
     */
    private fun openMapsViewFragment(location: LatLng? = null, marker: MarkerOptions? = null) {

        val mapFragment = (getMapViewFragment(location, marker, fab, container))
                .also {
                    mapView = it
                }.apply {
                    presenter = this@MainActivity
                }

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, mapFragment)
                .commit()
    }

    /**
     * check if we can animate the closing of the map,
     * otherwise just close
     */
    override fun closeMapView() {

        if (isLollipopOrLater()) {

            (mapView as DismissibleAnimation)
                    .dismiss {
                        removeMap()
                    }
        } else {
            removeMap()
        }

    }

    private fun removeMap() {
        supportFragmentManager
                .beginTransaction()
                .remove(mapView as MapViewFragment)
                .commit()
    }

    /**
     * @property MapViewFragment will call search.
     * Here we display a SimplePlacesSearchDialog https://github.com/edTheGuy00/SimplePlacesSearchDialog
     * @param latLngBounds search will be within these bounds.
     */
    override fun handleSearchFabClick(latLngBounds: LatLngBounds) {
        SimplePlacesSearchDialogBuilder(this)
                .setResultsFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
                .setLatLngBounds(latLngBounds)
                .setLocationListener(object : SimplePlacesSearchDialog.PlaceSelectedCallback {
                    override fun onPlaceSelected(place: Place) {

                        val marker = MarkerOptions().position(place.latLng).title(place.name.toString())
                        mapView?.addMarker(marker)
                        mapView?.zoomToLocation(place.latLng)
                        mapView?.displayPlaceCard(place)
                    }
                })
                .build()
                .show()
    }

    /**
     * close the mapview if opened.
     */
    override fun onBackPressed() {
        if (mapView != null && mapView!!.isOpened) {
            closeMapView()
            return
        }
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

}
