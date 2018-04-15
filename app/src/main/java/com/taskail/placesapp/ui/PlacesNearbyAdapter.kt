package com.taskail.placesapp.ui

import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.taskail.placesapp.R
import com.taskail.placesapp.SearchNearbyQuery
import com.taskail.placesapp.ui.PlacesNearbyAdapter.ItemsViewHolder
import kotlinx.android.synthetic.main.item_place.view.*

/**
 *Created by ed on 4/12/18.
 */

class PlacesNearbyAdapter(results: List<SearchNearbyQuery.SearchNearby>,
                          private val getDistanceString: (LatLng) -> String,
                          private val openResult: (SearchNearbyQuery.SearchNearby) -> Unit) :
        Adapter<ItemsViewHolder>() {

    var results: List<SearchNearbyQuery.SearchNearby> = results

    set(value) {
        field = value
        notifyDataSetChanged()
    }

    class ItemsViewHolder(itemView: View) : ViewHolder(itemView) {

        fun setItem(result: SearchNearbyQuery.SearchNearby,
                    getDistanceString: (LatLng) -> String,
                    openResult: (SearchNearbyQuery.SearchNearby) -> Unit) {

            with(itemView) {
                with(result) {
                    placeName.text = name()
                    val lat = geometry().location().lat()
                    val lng = geometry().location().lng()
                    if (lat != null && lng != null)
                    distanceFrom.text = getDistanceString.invoke(LatLng(lat, lng))


                    if (icon().isNotBlank()) {
                        Glide.with(itemView).load(icon()).into(circularImageView)
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
        holder.setItem(results[position],
                getDistanceString,
                openResult)
    }

    override fun getItemCount(): Int {
        return results.size
    }
}