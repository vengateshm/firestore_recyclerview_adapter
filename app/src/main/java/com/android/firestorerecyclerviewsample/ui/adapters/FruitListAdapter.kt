package com.android.firestorerecyclerviewsample.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.firestorerecyclerviewsample.R
import com.android.firestorerecyclerviewsample.model.Fruit
import com.android.firestorerecyclerviewsample.ui.adapters.FruitListAdapter.FruitItemVH
import com.bumptech.glide.Glide
import com.firebase.ui.common.ChangeEventType
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.android.synthetic.main.fruit_list_item.view.*

class FruitListAdapter(options: FirestoreRecyclerOptions<Fruit>) :
    FirestoreRecyclerAdapter<Fruit, FruitItemVH>(
        options
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FruitItemVH {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.fruit_list_item, parent, false)
        return FruitItemVH(view)
    }

    override fun onBindViewHolder(holder: FruitItemVH, position: Int, model: Fruit) {
        holder.bindFruit(model)
    }

    override fun onError(e: FirebaseFirestoreException) {
        Log.e("Adapter", e.message!!)
    }

    class FruitItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindFruit(fruit: Fruit) {
            itemView.tvName.text = fruit.name
            Glide.with(itemView.context).load(fruit.imgURL).into(itemView.ivImage);
        }
    }
}