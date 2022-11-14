package com.raassh.gemastik15.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.raassh.gemastik15.view.fragments.moderation.ContributionReportFragment
import com.raassh.gemastik15.view.fragments.moderation.UserReportFragment

private const val NUM_TABS = 2

class ModerationPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = NUM_TABS

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ContributionReportFragment()
            1 -> UserReportFragment()
            else -> ContributionReportFragment()
        }
    }
}