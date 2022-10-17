package com.raassh.gemastik15.view.fragments.contribution

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.auth0.android.jwt.JWT
import com.raassh.gemastik15.R
import com.raassh.gemastik15.adapter.PlaceAdapter
import com.raassh.gemastik15.databinding.FragmentContributionBinding
import com.raassh.gemastik15.utils.LinearSpaceItemDecoration
import com.raassh.gemastik15.utils.Resource
import com.raassh.gemastik15.utils.placeItemToEntity
import com.raassh.gemastik15.utils.showSnackbar
import com.raassh.gemastik15.view.activity.dashboard.DashboardViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContributionFragment : Fragment() {
    private val viewModel by viewModel<ContributionViewModel>()
    private var binding: FragmentContributionBinding? = null
    private val sharedViewModel by viewModel<DashboardViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContributionBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recentAdapter = PlaceAdapter(RecyclerView.HORIZONTAL).apply {
            onItemClickListener = { place ->
                val action = ContributionFragmentDirections.actionNavigationContributeToPlaceDetailFragment2(place)
                findNavController().navigate(action)
            }
        }

        val nearbyAdapter = PlaceAdapter(RecyclerView.HORIZONTAL).apply {
            onItemClickListener = { place ->
                val action = ContributionFragmentDirections.actionNavigationContributeToPlaceDetailFragment2(place)
                findNavController().navigate(action)
            }
        }

        binding?.apply {
            rvRecentlyVisited.adapter = recentAdapter
            rvRecentlyVisited.addItemDecoration(LinearSpaceItemDecoration(16, RecyclerView.HORIZONTAL))

            rvNearby.adapter = nearbyAdapter
            rvNearby.addItemDecoration(LinearSpaceItemDecoration(16, RecyclerView.HORIZONTAL))

            val comingSoonHandler = View.OnClickListener {
                root.showSnackbar(getString(R.string.feature_not_available))
            }

            cdGuidelines.setOnClickListener(comingSoonHandler)
            cdArticles.setOnClickListener(comingSoonHandler)
            cdNews.setOnClickListener(comingSoonHandler)
        }

        viewModel.apply {
            contributionCount.observe(viewLifecycleOwner) {
                binding?.tvTotalContribution?.text = getString(R.string.total_contribution, it.data?.contributionCount ?: 0)
            }

            recent.observe(viewLifecycleOwner) {
                if (it.isEmpty()) {
                    binding?.tvRecentEmpty?.visibility = View.VISIBLE
                    binding?.rvRecentlyVisited?.visibility = View.GONE
                } else {
                    binding?.tvRecentEmpty?.visibility = View.GONE
                    binding?.rvRecentlyVisited?.visibility = View.VISIBLE
                    recentAdapter.submitList(it)
                }
            }

            nearby.observe(viewLifecycleOwner) {
                if (it != null) {
                    when(it) {
                        is Resource.Error, is Resource.Loading -> {
                            binding?.tvNearbyEmpty?.visibility = View.VISIBLE
                            binding?.rvNearby?.visibility = View.GONE
                        }
                        is Resource.Success -> {
                            if (it.data.isNullOrEmpty()) {
                                binding?.tvNearbyEmpty?.visibility = View.VISIBLE
                                binding?.rvNearby?.visibility = View.GONE
                            } else {
                                binding?.tvNearbyEmpty?.visibility = View.GONE
                                binding?.rvNearby?.visibility = View.VISIBLE
                                nearbyAdapter.submitList(it.data.map {
                                    placeItemToEntity(it)
                                })
                            }
                        }
                    }
                }
            }
        }

        sharedViewModel.apply {
            getToken().observe(viewLifecycleOwner) {
                if (!it.isNullOrEmpty()) {
                    val jwt = JWT(it)
                    val userId = jwt.getClaim("id").asString() ?: ""
                    val username = jwt.getClaim("name").asString()

                    viewModel.setUserId(userId)
                    binding?.tvName?.text = username
                }
            }

            location.observe(viewLifecycleOwner) {
                viewModel.setLocation(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}