package com.taskail.placesapp.util

import android.annotation.SuppressLint
import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider

/**
 *Created by ed on 4/13/18.
 */

@SuppressLint("NewApi")
val CIRCULAR_OUTLINE: ViewOutlineProvider = object : ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {
        outline.setOval(view.paddingLeft,
                view.paddingTop,
                view.width - view.paddingRight,
                view.height - view.paddingBottom)
    }
}