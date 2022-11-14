package com.raassh.gemastik15.view.fragments.moderation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.raassh.gemastik15.databinding.FragmentContributionReportBinding
import com.raassh.gemastik15.utils.Resource
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ContributionReportFragment : Fragment() {
    private var binding: FragmentContributionReportBinding? = null
    private val moderationViewModel by sharedViewModel<ModerationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContributionReportBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moderationViewModel.contributionsReport.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    Log.d("TAG", "Loading")
                }
                is Resource.Success -> {
                    Log.d("TAG", "Success")
                    Log.d("TAG", "onViewCreated: ${it.data}")
                }
                is Resource.Error -> {
                    Log.d("TAG", "Error")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}