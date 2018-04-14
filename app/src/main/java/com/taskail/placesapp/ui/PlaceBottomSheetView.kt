package com.taskail.placesapp.ui

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.model.LatLng
import com.taskail.placesapp.R
import com.taskail.placesapp.data.models.Result
import com.taskail.placesapp.main.MainContract
import kotlinx.android.synthetic.main.bottom_sheet_place.*

/**
 *Created by ed on 4/13/18.
 */

class PlaceBottomSheetView : BottomSheetDialogFragment(), MainContract.BottomSheetView {

    override lateinit var presenter: MainContract.Presenter

    lateinit var result: Result

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.bottom_sheet_place, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        placeName.text = result.name
        typeOfPlace.text = result.types[0]

        mapButton.setOnClickListener {
            this.dismiss()
            val latLng = LatLng(result.geometry.location.lat, result.geometry.location.lng)
            presenter.viewLocationOnMap(latLng, result.name)
        }

        favoritesButton.setOnClickListener {
            presenter.saveToFavorites(result)
        }
    }
}