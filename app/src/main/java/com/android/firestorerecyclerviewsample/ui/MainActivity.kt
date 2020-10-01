package com.android.firestorerecyclerviewsample.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.firestorerecyclerviewsample.R
import com.android.firestorerecyclerviewsample.model.Fruit
import com.android.firestorerecyclerviewsample.ui.adapters.FruitListAdapter
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var fruitsAdapter: FruitListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val query = FirebaseFirestore.getInstance()
            .collection("Fruits")
            .orderBy("name")
        val options = FirestoreRecyclerOptions.Builder<Fruit>()
            .setQuery(query, Fruit::class.java)
            .build()
        fruitsAdapter = FruitListAdapter(options)

        rvList.layoutManager = LinearLayoutManager(this)
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
}