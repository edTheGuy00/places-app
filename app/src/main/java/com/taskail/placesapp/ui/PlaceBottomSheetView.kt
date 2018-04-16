package com.taskail.placesapp.ui

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.model.LatLng
import com.taskail.placesapp.R
import com.taskail.placesapp.SearchNearbyQuery
import com.taskail.placesapp.data.models.FavoritePlace
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

        /**
         * determine the type of place that we have and display it accordingly.
         */
        when(place) {
            is SearchNearbyQuery.SearchNearby -> {
                placeName.text = (place as SearchNearbyQuery.SearchNearby).name()
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

    /**
     * determine the type of place that we have and open it accordingly.
     */
    private fun mapClickHandler() {
        when(place) {
            is SearchNearbyQuery.SearchNearby -> {
                with((place as SearchNearbyQuery.SearchNearby)){
                    val lat = geometry().location().lat()
                    val lng = geometry().location().lng()
                    if (lat != null && lng != null ) {
                        val latLng = LatLng(lat, lng)
                        presenter.viewLocationOnMap(latLng, name())
                    }
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

    /**
     * determine the type of place that we have and save it accordingly.
     */
    private fun favButtonHandler() {
        when(place) {
            is SearchNearbyQuery.SearchNearby -> {
                presenter.saveToFavorites(place)
            }
            is FavoritePlace -> {
                presenter.deleteFavorite(place as FavoritePlace)
            }
        }
    }
}