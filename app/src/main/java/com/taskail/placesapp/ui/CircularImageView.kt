package com.taskail.placesapp.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.taskail.placesapp.util.CIRCULAR_OUTLINE
import com.taskail.placesapp.util.isLollipopOrLater

/**
 *Created by ed on 4/13/18.
 */

class CircularImageView(context: Context,
                        attributeSet: AttributeSet):
        ImageView(context, attributeSet) {

    init {
        if (isLollipopOrLater()) {
            outlineProvider = CIRCULAR_OUTLINE
            clipToOutline = true
        }

    }

}