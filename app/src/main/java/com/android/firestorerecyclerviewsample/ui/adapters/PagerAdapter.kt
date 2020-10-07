package com.android.firestorerecyclerviewsample.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.firestorerecyclerviewsample.ui.fragments.FirestoreRecyclerViewFragment
import com.android.firestorerecyclerviewsample.ui.fragments.FirestoreRecyclerViewPagingFragment

class PagerAdapter(private val numOfPages: Int, fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = numOfPages

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> FirestoreRecyclerViewFragment.newInstance()
        1 -> FirestoreRecyclerViewPagingFragment.newInstance()
        else -> Fragment()
    }
}