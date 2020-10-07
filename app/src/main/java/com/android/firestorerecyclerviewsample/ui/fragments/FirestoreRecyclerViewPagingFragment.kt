package com.android.firestorerecyclerviewsample.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.firestorerecyclerviewsample.R
import com.android.firestorerecyclerviewsample.model.Fruit
import com.android.firestorerecyclerviewsample.ui.adapters.FruitListAdapter
import com.android.firestorerecyclerviewsample.ui.adapters.FruitListPagingAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_recycler_view.*

class FirestoreRecyclerViewPagingFragment : Fragment() {

    private lateinit var fruitsAdapter: FruitListPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val query = FirebaseFirestore.getInstance()
            .collection("Fruits")
            .orderBy("name", Query.Direction.DESCENDING)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPrefetchDistance(5)
            .setPageSize(5) // No of items loaded from datasource
            .build()
        val options = FirestorePagingOptions.Builder<Fruit>()
            .setQuery(query, config, Fruit::class.java)
            .build()

        fruitsAdapter = FruitListPagingAdapter(options)

        rvList.layoutManager = LinearLayoutManager(requireActivity())
        rvList.adapter = fruitsAdapter
    }

    override fun onStart() {
        super.onStart()
        fruitsAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        fruitsAdapter.stopListening()
    }

    companion object {
        @JvmStatic
        fun newInstance() = FirestoreRecyclerViewPagingFragment()
    }
}