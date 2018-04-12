package com.taskail.placesapp.ui

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 *Created by ed on 4/12/18.
 */

class TabsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var fragmentList = ArrayList<Fragment>()
    private var fragmentTitle = ArrayList<String>()

    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        fragmentTitle.add(title)
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitle[position]
    }
}