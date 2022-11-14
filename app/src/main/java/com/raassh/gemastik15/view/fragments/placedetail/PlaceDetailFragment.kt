package com.raassh.gemastik15.view.fragments.placedetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.raassh.gemastik15.R
import com.raassh.gemastik15.adapter.FacilityReviewAdapter
import com.raassh.gemastik15.adapter.PlacePhotoAdapter
import com.raassh.gemastik15.adapter.ReviewAdapter
import com.raassh.gemastik15.api.response.FacilitiesItem
import com.raassh.gemastik15.api.response.PlaceDetailData
import com.raassh.gemastik15.api.response.ReviewData
import com.raassh.gemastik15.databinding.FragmentPlaceDetailBinding
import com.raassh.gemastik15.utils.*
import com.raassh.gemastik15.view.activity.dashboard.DashboardViewModel
import dev.chrisbanes.insetter.applyInsetter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

private val REVIEW_LIMIT = 7

class PlaceDetailFragment : Fragment() {
    private val viewModel by viewModel<PlaceDetailViewModel>()
    private var binding: FragmentPlaceDetailBinding? = null
    private val sharedViewModel by sharedViewModel<DashboardViewModel>()
    private var map: GoogleMap? = null
    private var reviews: List<ReviewData>? = null

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaceDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = binding?.fragmentMap?.getFragment<SupportMapFragment?>()
        mapFragment?.getMapAsync(callback)
        mapFragment?.view?.apply {
            importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS
            isFocusable = false
        }

        val place = PlaceDetailFragmentArgs.fromBundle(requireArguments()).place
        showLoading(true)

        binding?.apply {
            root.applyInsetter { type(statusBars = true, navigationBars = true) { padding() } }

            tvPlaceName.text = place.name
            tvPlaceType.text = requireContext().translatePlaceTypeNameToView(place.type)

            if (place.distance == -1.0) {
                tvPlaceDistance.visibility = View.INVISIBLE
                ivDot.visibility = View.INVISIBLE
            } else {
                tvPlaceDistance.text = getString(R.string.distance, place.distance.rounded(2))
            }

            btnMaps.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:${place.latitude},${place.longitude}?q=${place.latitude},${place.longitude}"))
                intent.setPackage("com.google.android.apps.maps")

                if (intent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(intent)
                }
            }

            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }

        viewModel.apply {
            detail.observe(viewLifecycleOwner) {
                if (it != null) {
                    when (it) {
                        is Resource.Loading -> {
                            showLoading(true)
                        }
                        is Resource.Success -> {
                            showLoading(false)
                            setPlaceDetail(it.data)
                        }
                        is Resource.Error -> {
                            binding?.root?.showSnackbar(
                                requireContext().translateErrorMessage(it.message)
                            )

                            findNavController().navigateUp()
                        }
                    }
                }
            }

            getReviews(place.id).observe(viewLifecycleOwner) {
                if (it != null) {
                    when (it) {
                        is Resource.Loading -> {
                            setLoadingReviews(true)
                        }
                        is Resource.Success -> {
                            setLoadingReviews(false)
                            setReviews(it.data)
                        }
                        is Resource.Error -> {
                            setLoadingReviews(false)
                            showEmptyReviews()
                            binding?.root?.showSnackbar(
                                requireContext().translateErrorMessage(it.message)
                            )
                        }
                    }
                }
            }
        }

        sharedViewModel.location.observe(viewLifecycleOwner) {
            viewModel.getDetail(place, it?.latitude, it?.longitude)
        }
    }

    private fun showLoading(loading: Boolean) {
        binding?.apply {
            if (loading) {
                pbLoading.visibility = View.VISIBLE
                content.visibility = View.GONE
                binding?.btnMaps?.isEnabled = false
            } else {
                pbLoading.visibility = View.GONE
                content.visibility = View.VISIBLE
                binding?.btnMaps?.isEnabled = true
            }
        }
    }

    private fun setPlaceDetail(detail: PlaceDetailData?) {
        if (detail == null) {
            return
        }

        binding?.apply {
            tvAddress.text = detail.address

            val facilitiesGroup = requireContext().getFacilitiesGroup(detail.facilities)

            showFacilityReviews(facilitiesGroup[0], rvMobilityFacilities, tvMobilityFacilitiesEmpty)
            showFacilityReviews(facilitiesGroup[1], rvVisualFacilities, tvVisualFacilitiesEmpty)
            showFacilityReviews(facilitiesGroup[2], rvAudioFacilities, tvAudioFacilitiesEmpty)

            btnAddReview.setOnClickListener {
                val action = PlaceDetailFragmentDirections.actionPlaceDetailFragmentToAddContributionFragment(detail)
                findNavController().navigate(action)
            }

            rvPhoto.apply {
                adapter = PlacePhotoAdapter().apply {
                    submitList(detail.photos)
                }
                addItemDecoration(LinearSpaceItemDecoration(16, RecyclerView.HORIZONTAL))
            }
        }

        val latLng = LatLng(detail.latitude, detail.longitude)
        map?.addMarker(MarkerOptions().position(latLng).title(detail.name))

        map?.setOnMapLoadedCallback {
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }

        map?.setContentDescription(null)
    }

    private fun showFacilityReviews(facilities: List<FacilitiesItem>, rvReviews: RecyclerView, tvEmpty: TextView) {
        if (facilities.isNotEmpty()) {
            val adapter = FacilityReviewAdapter()
            rvReviews.adapter = adapter
            rvReviews.addItemDecoration(GridSpaceItemDecoration(2, 8))
            adapter.submitList(facilities)
        } else {
            rvReviews.visibility = View.GONE
            tvEmpty.visibility = View.VISIBLE
        }
    }

    private fun setReviews(reviews: List<ReviewData>?) {
//        TODO: Filter reviews from the logged in user
//        this.reviews = reviews?.filter { it.user.id != sharedViewModel.user.value?.id }
        this.reviews = reviews

        if (this.reviews == null) {
            showEmptyReviews()
            return
        }

        if (this.reviews?.size!! <= REVIEW_LIMIT) {
            binding?.apply {
                btnSeeAllReviews.visibility = View.GONE
            }
        } else {
            binding?.apply {
                btnSeeAllReviews.visibility = View.VISIBLE
                btnSeeAllReviews.text = getString(R.string.see_all_reviews, this@PlaceDetailFragment.reviews?.size)
            }
        }

        binding?.rvReviews?.apply {
            adapter = ReviewAdapter().apply {
                submitList(this@PlaceDetailFragment.reviews!!.take(REVIEW_LIMIT))
            }
            addItemDecoration(LinearSpaceItemDecoration(16, RecyclerView.HORIZONTAL))
        }
    }

    private fun setLoadingReviews(loading: Boolean) {
        binding?.apply {
            if (loading) {
                pbLoadingReview.visibility = View.VISIBLE
                rvReviews.visibility = View.GONE
                tvReviewsEmpty.visibility = View.GONE
                btnSeeAllReviews.visibility = View.GONE
            } else {
                pbLoadingReview.visibility = View.GONE
                rvReviews.visibility = View.VISIBLE
                tvReviewsEmpty.visibility = View.GONE
                btnSeeAllReviews.visibility = View.VISIBLE
            }
        }
    }

    private fun showEmptyReviews() {
        binding?.apply {
            rvReviews.visibility = View.GONE
            tvReviewsEmpty.visibility = View.VISIBLE
            btnSeeAllReviews.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        map = null
    }
}