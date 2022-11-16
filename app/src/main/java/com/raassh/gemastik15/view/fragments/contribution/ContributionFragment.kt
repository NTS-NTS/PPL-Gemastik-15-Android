package com.raassh.gemastik15.view.fragments.contribution

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.auth0.android.jwt.JWT
import com.raassh.gemastik15.R
import com.raassh.gemastik15.adapter.PlaceAdapter
import com.raassh.gemastik15.adapter.ReviewHistoryAdapter
import com.raassh.gemastik15.api.response.ContributionUserData
import com.raassh.gemastik15.api.response.ContributionUserPlaceData
import com.raassh.gemastik15.api.response.PlaceDetailData
import com.raassh.gemastik15.databinding.FragmentContributionBinding
import com.raassh.gemastik15.local.db.PlaceEntity
import com.raassh.gemastik15.utils.*
import com.raassh.gemastik15.view.activity.dashboard.DashboardViewModel
import dev.chrisbanes.insetter.applyInsetter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContributionFragment : Fragment(), PopupMenu.OnMenuItemClickListener {
    private val viewModel by viewModel<ContributionViewModel>()
    private var binding: FragmentContributionBinding? = null
    private val sharedViewModel by sharedViewModel<DashboardViewModel>()
    private lateinit var userReview: ContributionUserPlaceData
    private lateinit var reviews: List<ContributionUserData>

    private val reviewHistoryAdapter = ReviewHistoryAdapter(true).apply {
        onItemClickListener = { contribution, holder ->
            userReview = contributionUserToUserPlace(contribution)
            showMenu(holder.btnOption)
        }
        onPlaceClickListener = { place ->
            val action =
                ContributionFragmentDirections.actionNavigationContributeToPlaceDetailFragment(
                    placeItemToEntity(place)
                )
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContributionBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recentAdapter = PlaceAdapter(RecyclerView.HORIZONTAL, true).apply {
            onItemClickListener = { place ->
                val action =
                    ContributionFragmentDirections.actionNavigationContributeToPlaceDetailFragment(
                        place
                    )
                findNavController().navigate(action)
            }
            btnOnItemClickListener = { place ->
                goToAddContribution(place)
            }
        }

        val nearbyAdapter = PlaceAdapter(RecyclerView.HORIZONTAL, true).apply {
            onItemClickListener = { place ->
                val action =
                    ContributionFragmentDirections.actionNavigationContributeToPlaceDetailFragment(
                        place
                    )
                findNavController().navigate(action)
            }
            btnOnItemClickListener = { place ->
                goToAddContribution(place)
            }
        }



        binding?.apply {
            root.applyInsetter { type(statusBars = true) { padding() } }

            rvRecentlyVisited.adapter = recentAdapter
            rvRecentlyVisited.addItemDecoration(
                LinearSpaceItemDecoration(
                    16,
                    RecyclerView.HORIZONTAL
                )
            )

            rvNearby.adapter = nearbyAdapter
            rvNearby.addItemDecoration(LinearSpaceItemDecoration(16, RecyclerView.HORIZONTAL))

            rvReviewHistory.apply {
                adapter = reviewHistoryAdapter

                if (itemDecorationCount == 0) {
                    addItemDecoration(LinearSpaceItemDecoration(16, RecyclerView.HORIZONTAL))
                }
            }

            cdGuidelines.setOnClickListener {
                val action = ContributionFragmentDirections.actionNavigationContributeToReadNav(2)
                findNavController().navigate(action)
            }

            cdArticles.setOnClickListener {
                val action = ContributionFragmentDirections.actionNavigationContributeToReadNav(1)
                findNavController().navigate(action)
            }

            cdNews.setOnClickListener {
                val action = ContributionFragmentDirections.actionNavigationContributeToReadNav(0)
                findNavController().navigate(action)
            }
        }

        viewModel.apply {
            contributionCount.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Error -> {
                        binding?.tvTotalContribution?.text =
                            getString(R.string.total_contribution,  0)

                        requireActivity().checkAuthError(it.message)
                    }
                    is Resource.Loading -> {
                        binding?.tvTotalContribution?.text =
                            getString(R.string.total_contribution,  0)
                    }
                    is Resource.Success -> {
                        binding?.tvTotalContribution?.text =
                            getString(R.string.total_contribution,  it.data?.contributionCount ?: 0)
                    }
                }
            }

            reviewedPlacesId.observe(viewLifecycleOwner) { reviewedPlacesId ->
                if (reviewedPlacesId != null) {
                    recent.observe(viewLifecycleOwner) {
                        val unreviewedPlaces = it.filter { place ->
                            !reviewedPlacesId.contains(place.id)
                        }
                        if (unreviewedPlaces.isEmpty()) {
                            binding?.tvRecentEmpty?.visibility = View.VISIBLE
                            binding?.rvRecentlyVisited?.visibility = View.GONE
                        } else {
                            binding?.tvRecentEmpty?.visibility = View.GONE
                            binding?.rvRecentlyVisited?.visibility = View.VISIBLE
                            recentAdapter.submitList(unreviewedPlaces)
                        }
                    }

                    nearby.observe(viewLifecycleOwner) {
                        if (it != null) {
                            when (it) {
                                is Resource.Error, is Resource.Loading -> {
                                    binding?.tvNearbyEmpty?.visibility = View.VISIBLE
                                    binding?.rvNearby?.visibility = View.GONE
                                }
                                is Resource.Success -> {
                                    val unreviewedPlaces = it.data?.filter { place ->
                                        !reviewedPlacesId.contains(place.id)
                                    }
                                    if (unreviewedPlaces.isNullOrEmpty()) {
                                        binding?.tvNearbyEmpty?.visibility = View.VISIBLE
                                        binding?.rvNearby?.visibility = View.GONE
                                    } else {
                                        binding?.tvNearbyEmpty?.visibility = View.GONE
                                        binding?.rvNearby?.visibility = View.VISIBLE
                                        nearbyAdapter.submitList(unreviewedPlaces.map { item ->
                                            placeItemToEntity(item)
                                        })
                                    }
                                }
                            }
                        } else {
                            binding?.tvNearbyEmpty?.visibility = View.VISIBLE
                            binding?.rvNearby?.visibility = View.GONE
                        }
                    }
                }
            }

        }

        sharedViewModel.apply {
            getToken().observe(viewLifecycleOwner) {
                if (!it.isNullOrEmpty()) {
                    val jwt = JWT(it)
                    val username = jwt.getClaim("name").asString()
                    val id = jwt.getClaim("id").asString()

                    viewModel.setToken(it)
                    if (id != null) {
                        viewModel.setUserId(id)
                    }
                    binding?.tvName?.text =
                        if (username.isNullOrEmpty()) resources.getString(R.string.your_contribution)
                        else username

                    getReviewHistory()
                }
            }

            location.observe(viewLifecycleOwner) {
                viewModel.setLocation(it)
            }
        }
    }

    private fun getReviewHistory() {
        viewModel.reviewHistory().observe(viewLifecycleOwner) { review ->
            if (review != null) {
                when (review) {
                    is Resource.Error -> {
                        binding?.apply {
                            llReviewHistory.visibility = View.GONE
                            llReviewRecommendation.visibility = View.VISIBLE
                            tvNearbyEmpty.visibility = View.VISIBLE
                            tvRecentEmpty.visibility = View.VISIBLE
                            rvRecentlyVisited.visibility = View.GONE
                            rvNearby.visibility = View.GONE
                            pbLoading.visibility = View.GONE
                            root.showSnackbar(
                                message = requireContext().translateErrorMessage(review.message),
                                anchor = binding?.root?.rootView?.findViewById(R.id.bottom_nav_view)
                            )
                        }
                    }
                    is Resource.Loading -> {
                        binding?.llReviewHistory?.visibility = View.GONE
                        binding?.llReviewRecommendation?.visibility = View.GONE
                        binding?.pbLoading?.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding?.llReviewRecommendation?.visibility = View.VISIBLE
                        binding?.pbLoading?.visibility = View.GONE
                        if (review.data.isNullOrEmpty()) {
                            binding?.llReviewHistory?.visibility = View.GONE
                        } else {
                            Log.d("ReviewHistory", review.data.toString())
                            binding?.apply {
                                llReviewHistory.visibility = View.VISIBLE
                                tvReviewHistoryCount.text =
                                    getString(R.string.review_history_count_message, review.data.size)

                                btnSeeAllReviews.text =
                                    getString(R.string.see_all_your_reviews, review.data.size)
                                btnSeeAllReviews.setOnClickListener {
                                    val action = ContributionFragmentDirections.actionNavigationContributeToReviewHistoryFragment(
                                        review.data.toTypedArray(), 0
                                    )
                                    findNavController().navigate(action)
                                }
                            }
                            reviewHistoryAdapter.apply{
                                submitList(review.data.take(10))
                                reviews = review.data

                                onCardClickListener = { position ->
                                    val action = ContributionFragmentDirections.actionNavigationContributeToReviewHistoryFragment(
                                        review.data.toTypedArray(), position
                                    )
                                    findNavController().navigate(action)
                                }
                            }
                        }
                        review.data?.let { data -> viewModel.setReviewedPlacesId(data) }
                    }
                }
            } else {
                binding?.tvNearbyEmpty?.visibility = View.VISIBLE
                binding?.rvNearby?.visibility = View.GONE
            }
        }
    }

    private fun goToAddContribution(place: PlaceEntity) {
        viewModel.getDetail(place).observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is Resource.Error -> {
                        binding?.root?.showSnackbar(
                            message = requireContext().translateErrorMessage(it.message),
                            anchor = binding?.root?.rootView?.findViewById(R.id.bottom_nav_view)
                        )
                    }
                    is Resource.Loading -> {
                        binding?.root?.showSnackbar(
                            message = getString(R.string.loading),
                            anchor = binding?.root?.rootView?.findViewById(R.id.bottom_nav_view)
                        )
                    }
                    is Resource.Success -> {
                        val action =
                            ContributionFragmentDirections.actionNavigationContributeToAddContributionFragment(
                                it.data as PlaceDetailData
                            )
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun showMenu(v: View) {
        PopupMenu(context, v).apply {
            setOnMenuItemClickListener(this@ContributionFragment)
            inflate(R.menu.review_history_menu)
            try {
                val field = PopupMenu::class.java.getDeclaredField("mPopup")
                field.isAccessible = true
                val menuHelper = field.get(this)
                val classPopupHelper = Class.forName(menuHelper.javaClass.name)
                val setForceIcons = classPopupHelper.getMethod("setForceShowIcon", Boolean::class.java)
                setForceIcons.invoke(menuHelper, true)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                show()
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_review -> {
                viewModel.deleteReview(userReview.place_id).observe(viewLifecycleOwner) {
                    if (it != null) {
                        when (it) {
                            is Resource.Error -> {
                                binding?.root?.showSnackbar(
                                    message = requireContext().translateErrorMessage(it.message),
                                    anchor = binding?.root?.rootView?.findViewById(R.id.bottom_nav_view)
                                )
                            }
                            is Resource.Loading -> {
                                binding?.root?.showSnackbar(
                                    message = getString(R.string.loading),
                                    anchor = binding?.root?.rootView?.findViewById(R.id.bottom_nav_view)
                                )
                            }
                            is Resource.Success -> {
                                binding?.root?.showSnackbar(
                                    message = getString(R.string.review_deleted),
                                    anchor = binding?.root?.rootView?.findViewById(R.id.bottom_nav_view)
                                )
                                val newReviews = reviews.filter { review -> review.place.id != userReview.place_id }
                                reviewHistoryAdapter.apply {
                                    submitList(newReviews.take(10))
                                    reviews = newReviews
                                }

                                viewModel.setReviewedPlacesId(newReviews)
                            }
                        }
                    }
                }
                true
            }
            R.id.edit_review -> {
                val action = ContributionFragmentDirections.actionNavigationContributeToEditContributionFragment(userReview)
                findNavController().navigate(action)
                true
            }
            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}