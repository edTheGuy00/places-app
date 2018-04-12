package com.taskail.placesapp.ui.animation

import android.support.v4.content.ContextCompat
import android.view.View
import com.taskail.placesapp.PlacesApplication
import com.taskail.placesapp.R

/**
 *Created by ed on 4/12/18.
 */

fun fabToFragmentReveal(fab: View, container: View): RevealAnimationSettings {
    val fabX = (fab.x + fab.width   /2).toInt()
    val fabY = (fab.y + fab.height / 2).toInt()

    val containerW = container.width
    val containerH = container.height

    return RevealAnimationSettings(fabX,
            fabY,
            containerW,
            containerH)
}

fun reveal(view: View, revealAnim: RevealAnimationSettings) {
    registerCircularRevealAnimation(view,
            revealAnim,
            ContextCompat.getColor(PlacesApplication.INSTANCE, R.color.colorPrimary),
            ContextCompat.getColor(PlacesApplication.INSTANCE, R.color.colorPrimaryLight))
}

fun exit(view: View,
         revealAnim: RevealAnimationSettings,
         dismiss: () -> Unit) {
    startCircularExitAnimation(view,
            revealAnim,
            ContextCompat.getColor(PlacesApplication.INSTANCE, R.color.colorPrimaryLight),
            ContextCompat.getColor(PlacesApplication.INSTANCE, R.color.colorPrimary),
            dismiss)
}