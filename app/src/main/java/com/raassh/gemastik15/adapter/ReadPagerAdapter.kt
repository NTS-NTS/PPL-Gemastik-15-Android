package com.raassh.gemastik15.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.raassh.gemastik15.view.fragments.read.ArticlesFragment
import com.raassh.gemastik15.view.fragments.read.GuidelinesFragment
import com.raassh.gemastik15.view.fragments.read.NewsFragment

private const val NUM_TABS = 3

class ReadPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = NUM_TABS

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NewsFragment()
            1 -> ArticlesFragment()
            2 -> GuidelinesFragment()
            else -> NewsFragment()
        }
    }
}