package com.raassh.gemastik15.view.fragments.moderation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.raassh.gemastik15.R
import com.raassh.gemastik15.adapter.ModerationPagerAdapter
import com.raassh.gemastik15.databinding.FragmentModerationBinding
import com.raassh.gemastik15.view.activity.dashboard.DashboardViewModel
import dev.chrisbanes.insetter.applyInsetter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ModerationFragment : Fragment() {
    private val moderationViewModel by sharedViewModel<ModerationViewModel>()
    private val sharedViewModel by sharedViewModel<DashboardViewModel>()
    private var binding: FragmentModerationBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentModerationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            root.applyInsetter { type(statusBars = true, navigationBars = true) { padding() } }

            viewPager.adapter = ModerationPagerAdapter(this@ModerationFragment)

            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = getString(R.string.contribution_report)
                    }
                    1 -> {
                        tab.text = getString(R.string.user_report)
                    }
                }
            }.attach()
        }

        sharedViewModel.getToken().observe(viewLifecycleOwner) {
            if (!it.isNullOrBlank()) {
                moderationViewModel.setToken(it)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}