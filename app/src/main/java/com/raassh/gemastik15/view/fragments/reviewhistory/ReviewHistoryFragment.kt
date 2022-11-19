package com.raassh.gemastik15.view.fragments.reviewhistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.raassh.gemastik15.R
import com.raassh.gemastik15.adapter.ReviewHistoryAdapter
import com.raassh.gemastik15.api.response.ContributionUserData
import com.raassh.gemastik15.api.response.ContributionUserPlaceData
import com.raassh.gemastik15.databinding.FragmentReviewHistoryBinding
import com.raassh.gemastik15.utils.*
import com.raassh.gemastik15.view.activity.dashboard.DashboardViewModel
import dev.chrisbanes.insetter.applyInsetter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewHistoryFragment : Fragment(), PopupMenu.OnMenuItemClickListener {
    private val viewModel by viewModel<ReviewHistoryViewModel>()
    private val sharedViewModel by sharedViewModel<DashboardViewModel>()
    private var binding: FragmentReviewHistoryBinding? = null
    private lateinit var userReview: ContributionUserPlaceData
    private lateinit var reviews: List<ContributionUserData>

    private val reviewAdapter = ReviewHistoryAdapter().apply {
        onItemClickListener = { contribution, holder ->
            userReview = contributionUserToUserPlace(contribution)
            showMenu(holder.btnOption)
        }
        onPlaceClickListener = { place ->
            val action =
                ReviewHistoryFragmentDirections.actionReviewHistoryFragmentToPlaceDetailFragment(
                    placeItemToEntity(place)
                )
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewHistoryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reviews = ReviewHistoryFragmentArgs.fromBundle(
            requireArguments()
        ).reviews.toList()
        val position = ReviewHistoryFragmentArgs.fromBundle(
            requireArguments()
        ).position

        binding?.apply {
            root.applyInsetter { type(statusBars = true, navigationBars = true) { padding() } }
            tvReviewCount.text = StringBuilder().append("(").append(reviews.size.toString()).append(")")
            pbLoading.visibility = View.VISIBLE
            rvReviewHistory.visibility = View.GONE

            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }

        sharedViewModel.apply {
            getToken().observe(viewLifecycleOwner) {
                if (!it.isNullOrEmpty()) {
                    viewModel.setToken(it)

                    binding?.rvReviewHistory?.apply {
                        adapter = reviewAdapter
                        reviewAdapter.submitList(reviews)
                        if (itemDecorationCount == 0) {
                            addItemDecoration(LinearSpaceItemDecoration(16, RecyclerView.VERTICAL, true))
                        }
                        scrollToPosition(position)
                    }

                    binding?.pbLoading?.visibility = View.GONE
                    binding?.rvReviewHistory?.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showMenu(v: View) {
        PopupMenu(context, v).apply {
            setOnMenuItemClickListener(this@ReviewHistoryFragment)
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
                            }
                            is Resource.Success -> {
                                val newReviews = reviews.filter { review ->
                                    review.place.id != userReview.place_id
                                }

                                reviewAdapter.submitList(newReviews)

                                binding?.root?.showSnackbar(
                                    message = getString(R.string.review_deleted),
                                )
                            }
                        }
                    }
                }
                true
            }
            R.id.edit_review -> {
                val action =
                    ReviewHistoryFragmentDirections.actionReviewHistoryFragmentToEditContributionFragment(
                        userReview
                    )
                findNavController().navigate(action)
                true
            }
            else -> false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}