package com.raassh.gemastik15.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.raassh.gemastik15.view.fragments.read.ArticlesFragment
import com.raassh.gemastik15.view.fragments.read.GuidelinesFragment
import com.raassh.gemastik15.view.fragments.read.NewsFragment
import com.raassh.gemastik15.view.fragments.searchuser.MessageSearchResultFragment
import com.raassh.gemastik15.view.fragments.searchuser.UserSearchResultFragment

private const val NUM_TABS = 2

class SearchUserPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = NUM_TABS

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MessageSearchResultFragment()
            1 -> UserSearchResultFragment()
            else -> MessageSearchResultFragment()
        }
    }
}