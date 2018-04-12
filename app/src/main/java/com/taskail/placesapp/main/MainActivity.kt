package com.taskail.placesapp.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.taskail.placesapp.R
import com.taskail.placesapp.ui.TabsPagerAdapter
import com.taskail.placesapp.util.favoritesString
import com.taskail.placesapp.util.nearbyString
import kotlinx.android.synthetic.main.layout_viewpage_list.*

class MainActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName

    private lateinit var pagerAdapter: TabsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pagerAdapter = TabsPagerAdapter(supportFragmentManager)
        setupViewPager()
    }

    private fun setupViewPager() {

        pagerAdapter.addFragment(NearbyFragment(), nearbyString())
        pagerAdapter.addFragment(FavoritesFragment(), favoritesString())

        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }
}
