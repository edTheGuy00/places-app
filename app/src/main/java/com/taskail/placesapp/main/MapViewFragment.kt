package com.taskail.placesapp.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.taskail.placesapp.R
import com.taskail.placesapp.ui.animation.*
import com.taskail.placesapp.util.supportsAnimation
import kotlinx.android.synthetic.main.fragment_map_view.*

/**
 *Created by ed on 4/12/18.
 */

class MapViewFragment : Fragment(),
        OnMapReadyCallback,
        MainContract.MapView,
        DismissibleAnimation
{

    override lateinit var presenter: MainContract.Presenter

    private lateinit var googleMap: GoogleMap

    override var isOpened: Boolean = false

    companion object {

        const val ARG_REVEAL = "args_reveal"

        /**
         * new instance for opening the fragment in regular mode
         */
        fun newInstance() : MapViewFragment {
            return MapViewFragment()
        }

        /**
         * new instance for opening the fragment with a circular reveal animation
         */
        fun newAnimatedInstance(revealAnimationSettings: RevealAnimationSettings) :
                MapViewFragment {
            val bundle = Bundle()
            bundle.putParcelable(ARG_REVEAL, revealAnimationSettings)
            val fragment = MapViewFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_map_view, container, false)

        if (supportsAnimation()) {
            val revealAnim: RevealAnimationSettings = arguments?.getParcelable(ARG_REVEAL)!!
            reveal(view, revealAnim)
        }

        isOpened = true

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (childFragmentManager
                .findFragmentById(R.id.map)
                as SupportMapFragment).apply {
            getMapAsync(this@MapViewFragment)
        }

        fabSearch.setOnClickListener {
            presenter.handleSearchFabClick()
        }
    }

    /**
     * we display the users location in a small blue dot.
     * we do not need to check for permission since we will ask the
     * @property presenter if the location permission has been granted
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        with(googleMap) {
            this@MapViewFragment.googleMap = this
            if (presenter.isLocationGranted()) {
                isMyLocationEnabled = true
                presenter.requestLocation {
                    zoomToLocation(it)
                }
            }
        }
    }

    /**
     * this function will be passed to the presenter via
     * @property presenter.requestLocation
     */
    override fun zoomToLocation(myLocation: LatLng){
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 18f))
    }

    override fun addMarker(marker: MarkerOptions) {
        googleMap.addMarker(marker)
    }

    /**
     * a higher-order function that will dismiss this fragment.
     */
    override fun dismiss(dismiss: () -> Unit) {
        if (supportsAnimation()) {
            val revealAnim: RevealAnimationSettings = arguments?.getParcelable(ARG_REVEAL)!!
            exit(view!!, revealAnim, dismiss)
        } else {
            dismiss.invoke()
        }

        isOpened = false
    }
}