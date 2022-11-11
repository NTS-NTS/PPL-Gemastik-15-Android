package com.raassh.gemastik15.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.raassh.gemastik15.view.fragments.read.ArticleFragment
import com.raassh.gemastik15.view.fragments.read.NewsFragment

private const val NUM_TABS = 2

class ViewPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = NUM_TABS

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NewsFragment()
            1 -> ArticleFragment()
            else -> NewsFragment()
        }
    }
}