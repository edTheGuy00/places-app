package com.taskail.placesapp.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.placesapp.R
import com.taskail.placesapp.data.models.FavoritePlace
import com.taskail.placesapp.ui.FavoritePlacesAdapter
import kotlinx.android.synthetic.main.fragment_recycler.*

/**
 *Created by ed on 4/12/18.
 */

class FavoritesFragment : Fragment(), MainContract.FavoritesView {

    override lateinit var presenter: MainContract.Presenter

    lateinit var adapter: FavoritePlacesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_recycler, container, false)

        adapter = FavoritePlacesAdapter(ArrayList(0), {
            presenter.openBottomSheet(it)
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter

        presenter.getFavoritePlaces()
    }

    override fun displayFavorites(favorites: List<FavoritePlace>) {
        adapter.favorites = favorites
    }
}