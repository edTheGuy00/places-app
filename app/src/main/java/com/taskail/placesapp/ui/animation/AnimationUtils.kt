package com.taskail.placesapp.ui.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewAnimationUtils
import android.animation.ArgbEvaluator
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import com.taskail.placesapp.util.isLollipopOrLater


/**
 *Created by ed on 3/28/18.
 */

/**
 * circular reveal suitable for Floating Action Buttons,
 * created following: https://medium.com/@gabornovak/circular-reveal-animation-between-fragments-d8ed9011aec
 */
private const val animationDuration = 860.toLong()

@SuppressLint("NewApi")
fun registerCircularRevealAnimation(view: View,
                                    revealSettings: RevealAnimationSettings,
                                    startColor: Int,
                                    endColor: Int){

    if (isLollipopOrLater()) {
        view.addOnLayoutChangeListener( object : View.OnLayoutChangeListener {

            override fun onLayoutChange(p0: View?,
                                        p1: Int,
                                        p2: Int,
                                        p3: Int,
                                        p4: Int,
                                        p5: Int,
                                        p6: Int,
                                        p7: Int,
                                        p8: Int) {
                view.removeOnLayoutChangeListener(this)

                val cx = revealSettings.centerX
                val cy = revealSettings.centerY
                val width = revealSettings.width
                val height = revealSettings.height


                val finalRadius: Float = Math.sqrt((width * width + height * height).toDouble()).toFloat()
                val anim = ViewAnimationUtils.createCircularReveal(p0, cx, cy, 0f, finalRadius).setDuration(animationDuration)
                anim.interpolator = FastOutSlowInInterpolator()
                anim.start()
                startColorAnimation(view, startColor, endColor, animationDuration.toInt())
            }

        })
    }

}

@SuppressLint("NewApi")
fun startCircularExitAnimation(view: View,
                               revealSettings: RevealAnimationSettings,
                               startColor: Int,
                               endColor: Int,
                               dismiss: () -> Unit) {

    if (isLollipopOrLater()) {
        val cx = revealSettings.centerX
        val cy = revealSettings.centerY
        val width = revealSettings.width
        val height = revealSettings.height


        val initRadius: Float = Math.sqrt((width * width + height * height).toDouble()).toFloat()
        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initRadius, 0f).setDuration(animationDuration)
        anim.duration = animationDuration
        anim.interpolator = FastOutSlowInInterpolator()
        anim.addListener( object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                view.visibility = View.GONE
                dismiss.invoke()
            }
        })

        anim.start()
        startColorAnimation(view, startColor, endColor, animationDuration.toInt())
    } else {
        dismiss.invoke()
    }
}



fun startColorAnimation(view: View, startColor: Int, endColor: Int, duration: Int) {
    val anim = ValueAnimator()
    anim.setIntValues(startColor, endColor)
    anim.setEvaluator(ArgbEvaluator())
    anim.addUpdateListener { valueAnimator -> view.setBackgroundColor(valueAnimator.animatedValue as Int) }
    anim.duration = duration.toLong()
    anim.start()
}


