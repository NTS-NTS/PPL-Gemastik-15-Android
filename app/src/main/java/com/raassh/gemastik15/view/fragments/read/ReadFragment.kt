package com.raassh.gemastik15.view.fragments.read

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.google.android.material.tabs.TabLayoutMediator
import com.raassh.gemastik15.R
import com.raassh.gemastik15.adapter.ReadPagerAdapter
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

        val tabIndex = ReadFragmentArgs.fromBundle(requireArguments()).tabIndex

        binding?.apply {
            root.applyInsetter { type(statusBars = true) { padding() } }

            viewPager.adapter = ReadPagerAdapter(this@ReadFragment)

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = getString(R.string.news)
                        tab.icon = getDrawable(requireContext(), R.drawable.ic_baseline_news_28)
                    }
                    1 -> {
                        tab.text = getString(R.string.articles)
                        tab.icon = getDrawable(requireContext(), R.drawable.ic_outline_article_28)
                    }
                    2 -> {
                        tab.text = getString(R.string.guidelines)
                        tab.icon = getDrawable(requireContext(), R.drawable.ic_outline_guideline_28)
                    }
                }
            }.attach()

            viewPager.currentItem = tabIndex
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}