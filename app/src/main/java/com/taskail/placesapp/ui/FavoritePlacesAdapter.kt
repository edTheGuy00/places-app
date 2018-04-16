package com.taskail.placesapp.ui

import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.taskail.placesapp.GetMyPlacesQuery
import com.taskail.placesapp.R
import com.taskail.placesapp.data.models.FavoritePlace
import com.taskail.placesapp.ui.FavoritePlacesAdapter.ItemsViewHolder
import kotlinx.android.synthetic.main.item_place.view.*

/**
 *Created by ed on 4/12/18.
 */

class FavoritePlacesAdapter(favorites: List<GetMyPlacesQuery.Place>,
                            private val getDistanceString: (LatLng) -> String,
                            private val openResult: (GetMyPlacesQuery.Place) -> Unit) :
        Adapter<ItemsViewHolder>() {

    var favorites: List<GetMyPlacesQuery.Place> = favorites

    set(value) {
        field = value
        notifyDataSetChanged()
    }

    class ItemsViewHolder(itemView: View) : ViewHolder(itemView) {

        fun setItem(favorite: GetMyPlacesQuery.Place,
                    getDistanceString: (LatLng) -> String,
                    openResult: (GetMyPlacesQuery.Place) -> Unit) {

            with(itemView) {
                with(favorite) {
                    placeName.text = name()

                    // users current location is  not available right away.
                    distanceFrom.text = getDistanceString(LatLng(lat(), lng()))

                    if (image() != null) {
                        Glide.with(itemView).load(image()).into(circularImageView)
                    }

                    setOnClickListener {
                        openResult(this)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_place, parent, false)

        return ItemsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.setItem(favorites[position],
                getDistanceString,
                openResult)
    }

    override fun getItemCount(): Int {
        return favorites.size
    }
}