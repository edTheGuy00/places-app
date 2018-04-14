package com.taskail.placesapp.ui

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.model.LatLng
import com.taskail.placesapp.R
import com.taskail.placesapp.data.models.FavoritePlace
import com.taskail.placesapp.data.models.Result
import com.taskail.placesapp.main.MainContract
import kotlinx.android.synthetic.main.bottom_sheet_place.*

/**
 *Created by ed on 4/13/18.
 */

class PlaceBottomSheetView : BottomSheetDialogFragment(), MainContract.BottomSheetView {

    override lateinit var presenter: MainContract.Presenter

    var place = Any()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.bottom_sheet_place, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when(place) {
            is Result -> {
                placeName.text = (place as Result).name
                typeOfPlace.text = (place as Result).types[0]
                favoritesButton.setText(R.string.add_to_favorites)
            }
            is FavoritePlace -> {
                placeName.text = (place as FavoritePlace).name
                favoritesButton.setText(R.string.remove_from_fav)
            }
        }


        mapButton.setOnClickListener {
            this.dismiss()
            mapClickHandler()
        }

        favoritesButton.setOnClickListener {
            this.dismiss()
            favButtonHandler()
        }
    }

    private fun mapClickHandler() {
        when(place) {
            is Result -> {
                with((place as Result)){
                    val latLng = LatLng(geometry.location.lat, geometry.location.lng)
                    presenter.viewLocationOnMap(latLng, name)
                }
            }
            is FavoritePlace -> {
                with((place as FavoritePlace)){
                    val latLng = LatLng(lat, lng)
                    presenter.viewLocationOnMap(latLng, name)
                }
            }
        }
    }

    private fun favButtonHandler() {
        when(place) {
            is Result -> {
                presenter.saveToFavorites(place)
            }
            is FavoritePlace -> {
                presenter.deleteFavorite(place as FavoritePlace)
            }
        }
    }
}