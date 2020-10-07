package com.android.firestorerecyclerviewsample.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.firestorerecyclerviewsample.R
import com.android.firestorerecyclerviewsample.model.Fruit
import com.android.firestorerecyclerviewsample.ui.adapters.FruitListAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_recycler_view.*

class FirestoreRecyclerViewFragment : Fragment() {

    private lateinit var fruitsAdapter: FruitListAdapter

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
            .orderBy("name")
        val options = FirestoreRecyclerOptions.Builder<Fruit>()
            .setQuery(query, Fruit::class.java)
            .build()
        fruitsAdapter = FruitListAdapter(options)

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
        fun newInstance() = FirestoreRecyclerViewFragment()
    }
}