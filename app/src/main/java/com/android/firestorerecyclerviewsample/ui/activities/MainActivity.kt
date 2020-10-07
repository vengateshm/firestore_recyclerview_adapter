package com.android.firestorerecyclerviewsample.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.firestorerecyclerviewsample.R
import com.android.firestorerecyclerviewsample.ui.adapters.PagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pager.adapter = PagerAdapter(2, this)
        TabLayoutMediator(tabLayout, pager) { tab, position ->
            tab.text = when (position) {
                0 -> "Without Paging"
                1 -> "With Paging"
                else -> ""
            }
        }.attach()
    }
}