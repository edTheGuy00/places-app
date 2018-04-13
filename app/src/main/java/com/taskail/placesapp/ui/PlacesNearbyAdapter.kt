package com.taskail.placesapp.ui

import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.taskail.placesapp.R
import com.taskail.placesapp.data.models.Geometry
import com.taskail.placesapp.data.models.Result
import com.taskail.placesapp.ui.PlacesNearbyAdapter.ItemsViewHolder
import kotlinx.android.synthetic.main.item_place.view.*

/**
 *Created by ed on 4/12/18.
 */

class PlacesNearbyAdapter(results: List<Result>,
                          private val getDistanceString: (Geometry) -> String,
                          private val openResult: (Result) -> Unit) :
        Adapter<ItemsViewHolder>() {

    var results: List<Result> = results

    set(value) {
        field = value
        notifyDataSetChanged()
    }

    class ItemsViewHolder(itemView: View) : ViewHolder(itemView) {

        fun setItem(result: Result,
                    getDistanceString: (Geometry) -> String,
                    openResult: (Result) -> Unit) {

            with(itemView) {
                with(result) {
                    placeName.text = name
                    distanceFrom.text = getDistanceString.invoke(geometry)

                    if (icon.isNotBlank()) {
                        Glide.with(itemView).load(icon).into(circularImageView)
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