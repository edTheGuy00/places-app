package com.taskail.placesapp.util

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.preference.PreferenceManager
import com.taskail.placesapp.PlacesApplication

/**
 *Created by ed on 4/15/18.
 */

private fun edit(): SharedPreferences.Editor {
    return getPreferences().edit()
}

private fun getPreferences(): SharedPreferences {
    return PreferenceManager.getDefaultSharedPreferences(getContext())
}

private fun getResources(): Resources? {
    return getContext()?.resources
}

private fun getContext(): Context? {
    return PlacesApplication.INSTANCE
}

fun getString(key: String, defaultValue: String?): String? {
    return getPreferences().getString(key, defaultValue)
}

fun setString(key: String, value: String?) {
    edit().putString(key, value).apply()
}