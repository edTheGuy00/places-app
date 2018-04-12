package com.taskail.placesapp.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.taskail.placesapp.BackPressedHandler
import com.taskail.placesapp.R

/**
 *Created by ed on 4/12/18.
 */

class MapViewFragment : Fragment(), OnMapReadyCallback, MainContract.MapView {

    override lateinit var presenter: MainContract.Presenter

    companion object {

        fun newInstance() : MapViewFragment {
            return MapViewFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (childFragmentManager
                .findFragmentById(R.id.map)
                as SupportMapFragment).apply {
            getMapAsync(this@MapViewFragment)
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {

    }

    override fun onBackPressed(): Boolean {
        presenter.closeMapView()
        return true
    }
}