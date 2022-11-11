package com.raassh.gemastik15.view.fragments.read

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.raassh.gemastik15.adapter.ViewPagerAdapter
import com.raassh.gemastik15.databinding.FragmentReadBinding
import dev.chrisbanes.insetter.applyInsetter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReadFragment : Fragment() {
    private val viewModel by viewModel<ReadViewModel>()
    private var binding: FragmentReadBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReadBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            root.applyInsetter { type(statusBars = true) { padding() } }

            viewPager.adapter = ViewPagerAdapter(this@ReadFragment)

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = "News"
                    1 -> tab.text = "Article"
                }
            }.attach()
        }
    }
}