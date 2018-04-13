package com.taskail.placesapp.ui

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.placesapp.R
import com.taskail.placesapp.main.MainContract

/**
 *Created by ed on 4/13/18.
 */

class PlaceBottomSheetView : BottomSheetDialogFragment(), MainContract.BottomShetView {

    override lateinit var presenter: MainContract.Presenter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.bottom_sheet_place, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}