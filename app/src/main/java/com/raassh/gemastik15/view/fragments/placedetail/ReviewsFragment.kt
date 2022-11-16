package com.raassh.gemastik15.view.fragments.placedetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.raassh.gemastik15.adapter.ReviewAdapter
import com.raassh.gemastik15.databinding.FragmentReviewsBinding
import com.raassh.gemastik15.utils.*
import dev.chrisbanes.insetter.applyInsetter

class ReviewsFragment : Fragment() {
    private var binding: FragmentReviewsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reviews = ReviewsFragmentArgs.fromBundle(requireArguments()).reviews.toList()
        val position = ReviewsFragmentArgs.fromBundle(requireArguments()).position

        binding?.apply {
            root.applyInsetter { type(statusBars = true, navigationBars = true) { padding() } }
            tvReviewCount.text = StringBuilder().append("(").append(reviews.size.toString()).append(")")

            rvReviews.apply {
                adapter = ReviewAdapter().apply {
                    submitList(reviews)
                    onItemClickListener = { review ->
                        findNavController().navigate(ReviewsFragmentDirections.actionReviewsFragmentToReportReviewFragment(review))
                    }
                }
                addItemDecoration(LinearSpaceItemDecoration(16, RecyclerView.VERTICAL, true))
                scrollToPosition(position)
            }

            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}