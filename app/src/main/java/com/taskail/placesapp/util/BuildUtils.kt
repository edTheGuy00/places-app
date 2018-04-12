package com.taskail.placesapp.util

import android.os.Build

/**
 *Created by ed on 4/12/18.
 */

fun supportsAnimation() : Boolean {
    return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
}