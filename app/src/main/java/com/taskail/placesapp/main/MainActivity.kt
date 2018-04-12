package com.taskail.placesapp.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.taskail.placesapp.R
import com.taskail.placesapp.ui.TabsPagerAdapter
import com.taskail.placesapp.util.favoritesString
import com.taskail.placesapp.util.nearbyString
import kotlinx.android.synthetic.main.layout_viewpage_list.*

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

    private fun setupViewPager() {

        pagerAdapter.addFragment(NearbyFragment(), nearbyString())
        pagerAdapter.addFragment(FavoritesFragment(), favoritesString())

        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun openMapsViewFragment() {
        val mapFragment = MapViewFragment.newInstance()
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
        supportFragmentManager
                .beginTransaction()
                .remove(mapView as MapViewFragment)
                .commit()
    }

    override fun onBackPressed() {
        if (mapView.onBackPressed()) {
            return
        }
        super.onBackPressed()
    }
}
