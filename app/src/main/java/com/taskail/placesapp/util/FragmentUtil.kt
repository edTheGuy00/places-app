package com.taskail.placesapp.util

import android.view.View
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.taskail.placesapp.main.MapViewFragment
import com.taskail.placesapp.ui.animation.fabToFragmentReveal

/**
 *Created by ed on 4/13/18.
 */

/**
 * create a new
 * @property MapViewFragment
 * @param location a nullable LatLng
 * @param marker a nullable Marker
 *
 * based on these two params and the current Android build version we
 * return the appropriate MapView Object.
 */
fun getMapViewFragment(location: LatLng?,
                       marker: MarkerOptions?,
                       fab: View, container: View):
        MapViewFragment {

    return if (isLollipopOrLater())
        if (location != null) {
            MapViewFragment.newAnimatedInstance(fabToFragmentReveal(fab, container)).apply {
                this.location = location
                this.marker = marker
            }
        } else {
            MapViewFragment.newAnimatedInstance(fabToFragmentReveal(fab, container))
        }
    else
        if (location != null) {
            MapViewFragment.newInstance().apply {
                this.location = location
                this.marker = marker
            }
        } else {
            MapViewFragment.newInstance()
        }

}