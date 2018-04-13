package com.taskail.placesapp.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.placesapp.R
import com.taskail.placesapp.data.models.Result
import com.taskail.placesapp.ui.PlacesNearbyAdapter
import kotlinx.android.synthetic.main.fragment_recycler.*

/**
 *Created by ed on 4/12/18.
 */

class NearbyFragment : Fragment(), MainContract.NearbyView {

    private val TAG = javaClass.simpleName

    override lateinit var presenter: MainContract.Presenter

    lateinit var adapter: PlacesNearbyAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_recycler, container, false)

        adapter = PlacesNearbyAdapter(ArrayList(0),
                presenter.calculateDistance())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter

        Log.d(TAG, "nearby fragment created")

        presenter.fetchNearbyResults()
    }

    override fun displayResults(results: List<Result>) {
        adapter.results = results
    }

    override fun resultHasBeenLoaded(): Boolean {
        return adapter.results.isNotEmpty()
    }

}