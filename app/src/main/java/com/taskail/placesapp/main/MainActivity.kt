package com.taskail.placesapp.main

import android.location.Location
import android.os.Bundle
import android.util.Log
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.Place
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.taskail.googleplacessearchdialog.SimplePlacesSearchDialog
import com.taskail.googleplacessearchdialog.SimplePlacesSearchDialogBuilder
import com.taskail.placesapp.R
import com.taskail.placesapp.getRepository
import com.taskail.placesapp.location.LocationServiceActivity
import com.taskail.placesapp.ui.TabsPagerAdapter
import com.taskail.placesapp.ui.animation.DismissibleAnimation
import com.taskail.placesapp.ui.animation.fabToFragmentReveal
import com.taskail.placesapp.util.favoritesString
import com.taskail.placesapp.util.nearbyString
import com.taskail.placesapp.util.supportsAnimation
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_viewpage_list.*

/**
 * the main entry point and the presenter for the application.
 */

class MainActivity : LocationServiceActivity(), MainContract.Presenter {

    private val TAG = javaClass.simpleName

    private lateinit var pagerAdapter: TabsPagerAdapter
    private lateinit var mapView: MainContract.MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pagerAdapter = TabsPagerAdapter(supportFragmentManager)
        setupViewPager()

        fab.setOnClickListener {
            openMapsViewFragment()
        }
    }

    override fun lastKnowLocation(location: Location) {
        Log.d(TAG, "location received")

        val disposable = CompositeDisposable()

        val repo = getRepository(disposable)

        repo.getNearbyPlaces("establishment",
                "${location.latitude},${location.longitude}",
                1000,
                {
                    Log.d(TAG, it.results[0].name)
                })
    }

    override fun requestLocation(zoomToLocation: (LatLng) -> Unit) {
        getAccurateLocation {
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

        pagerAdapter.addFragment(NearbyFragment(), nearbyString())
        pagerAdapter.addFragment(FavoritesFragment(), favoritesString())

        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    /**
     * Fab button opens up the mapView.
     * Mapview will have a circular animation for
     * devices running Android 5.0 and above.
     */
    private fun openMapsViewFragment() {
        val mapFragment = (if (supportsAnimation())
            MapViewFragment
                    .newAnimatedInstance(
                            fabToFragmentReveal(fab, container)
                    )
        else
            MapViewFragment.newInstance()
                )
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

    override fun closeMapView() {

        if (supportsAnimation()) {

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

    override fun handleSearchFabClick() {
        SimplePlacesSearchDialogBuilder(this)
                .setResultsFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
                .setLocationListener(object : SimplePlacesSearchDialog.PlaceSelectedCallback {
                    override fun onPlaceSelected(place: Place) {

                        val marker = MarkerOptions().position(place.latLng).title(place.name.toString())
                        mapView.addMarker(marker)
                        mapView.zoomToLocation(place.latLng)
                    }
                })
                .build()
                .show()
    }

    override fun onBackPressed() {
        if (mapView.isOpened) {
            closeMapView()
            return
        }
        super.onBackPressed()
    }
}
