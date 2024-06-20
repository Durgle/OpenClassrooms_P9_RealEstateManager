package com.openclassrooms.realestatemanager.ui.estate

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.openclassrooms.realestatemanager.ui.estate.list.EstateListFragment
import com.openclassrooms.realestatemanager.ui.estate.list.EstateMapFragment

class EstateViewPagerAdapter(val fragment: EstateFragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> EstateListFragment.newInstance()
            1 -> EstateMapFragment.newInstance()
            else -> EstateListFragment.newInstance()
        }
    }
}