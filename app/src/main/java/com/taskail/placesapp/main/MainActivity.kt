package com.taskail.placesapp.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.taskail.placesapp.R
import com.taskail.placesapp.ui.TabsPagerAdapter
import com.taskail.placesapp.ui.animation.DismissibleAnimation
import com.taskail.placesapp.ui.animation.fabToFragmentReveal
import com.taskail.placesapp.util.favoritesString
import com.taskail.placesapp.util.nearbyString
import com.taskail.placesapp.util.supportsAnimation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_viewpage_list.*

/**
 * the main entry point and the presenter for the application.
 */

class MainActivity : AppCompatActivity(), MainContract.Presenter {

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

    override fun onBackPressed() {
        if (mapView.isOpened) {
            closeMapView()
            return
        }
        super.onBackPressed()
    }
}
