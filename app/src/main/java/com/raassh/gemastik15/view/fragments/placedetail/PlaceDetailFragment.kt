package com.raassh.gemastik15.view.fragments.placedetail

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.auth0.android.jwt.JWT
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.color.MaterialColors
import com.raassh.gemastik15.R
import com.raassh.gemastik15.adapter.FacilityReviewAdapter
import com.raassh.gemastik15.adapter.PlacePhotoAdapter
import com.raassh.gemastik15.adapter.ReviewAdapter
import com.raassh.gemastik15.adapter.SingleReviewFacilitiesAdapter
import com.raassh.gemastik15.api.response.ContributionUserPlaceData
import com.raassh.gemastik15.api.response.FacilitiesItem
import com.raassh.gemastik15.api.response.PlaceDetailData
import com.raassh.gemastik15.api.response.ReviewData
import com.raassh.gemastik15.databinding.FragmentPlaceDetailBinding
import com.raassh.gemastik15.local.db.PlaceEntity
import com.raassh.gemastik15.utils.*
import com.raassh.gemastik15.view.activity.dashboard.DashboardViewModel
import dev.chrisbanes.insetter.applyInsetter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val REVIEW_LIMIT = 7

class PlaceDetailFragment : Fragment() {
    private val viewModel by viewModel<PlaceDetailViewModel>()
    private var binding: FragmentPlaceDetailBinding? = null
    private val sharedViewModel by sharedViewModel<DashboardViewModel>()
    private var map: GoogleMap? = null
    private lateinit var place: PlaceEntity

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

        place = PlaceDetailFragmentArgs.fromBundle(requireArguments()).place
        showLoading(true)
        showEmptyReviews()
        showEmptyUserReview()

        binding?.apply {
            root.applyInsetter { type(statusBars = true, navigationBars = true) { padding() } }
            btnFavorite.isEnabled = false
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
            isFavorite.observe(viewLifecycleOwner) {
                setFavorite(it)
            }

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

            userId.observe(viewLifecycleOwner) { userId ->
                if (userId != null) {
                    getReviews(place.id).observe(viewLifecycleOwner) {
                        if (it != null) {
                            when (it) {
                                is Resource.Loading -> {
                                    setLoadingReviews(true)
                                }
                                is Resource.Success -> {
                                    setLoadingReviews(false)
                                    setReviews(it.data!!, userId)
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

                    getUserReview(place.id, userId).observe(viewLifecycleOwner) {
                        if (it != null) {
                            when (it) {
                                is Resource.Loading -> {

                                }
                                is Resource.Success -> {
                                    setUserReview(it.data!!)
                                }
                                is Resource.Error -> {
                                    showEmptyUserReview()
                                    binding?.root?.showSnackbar(
                                        requireContext().translateErrorMessage(it.message)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        sharedViewModel.apply {
            getToken().observe(viewLifecycleOwner) { it ->
                if (!it.isNullOrEmpty()) {
                    val jwt = JWT(it)
                    val userId = jwt.getClaim("id").asString()

                    viewModel.setToken(it)

                    if (userId != null) {
                        viewModel.setUserId(userId)
                    }

                    viewModel.getFavoritePlaces().observe(viewLifecycleOwner) { places ->
                        if (places != null) {
                            when (places) {
                                is Resource.Loading -> {
                                    binding?.btnFavorite?.isEnabled = false
                                }
                                is Resource.Success -> {
                                    val isFavorite = places.data?.find { favoritePlace ->
                                        favoritePlace.id == place.id
                                    } != null
                                    binding?.btnFavorite?.isEnabled = true
                                    viewModel.isFavorite.value = isFavorite
                                }
                                is Resource.Error -> {
                                    binding?.root?.showSnackbar(
                                        requireContext().translateErrorMessage(places.message)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            location.observe(viewLifecycleOwner) {
                viewModel.getDetail(place, it?.latitude, it?.longitude)
            }
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
                if (itemDecorationCount == 0) {
                    addItemDecoration(LinearSpaceItemDecoration(16, RecyclerView.HORIZONTAL))
                }
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

    private fun setReviews(_reviews: List<ReviewData>, userId: String) {
        val reviews = _reviews.filter { it.user.id != userId }

        if (reviews.isEmpty()) {
            showEmptyReviews()
            return
        }

        binding?.apply {
            rvReviews.visibility = View.VISIBLE
            tvReviewsEmpty.visibility = View.GONE
            btnSeeAllReviews.visibility = View.VISIBLE
        }

        binding?.btnSeeAllReviews?.setOnClickListener {
            findNavController().navigate(PlaceDetailFragmentDirections.actionPlaceDetailFragmentToReviewsFragment(
                reviews.toTypedArray(), 0
            ))
        }
        binding?.btnSeeAllReviews?.text = getString(R.string.see_all_reviews, reviews.size)

        binding?.rvReviews?.apply {
            adapter = ReviewAdapter(true).apply {
                submitList(reviews.take(REVIEW_LIMIT))
                onCardClickListener = { position ->
                    findNavController().navigate(PlaceDetailFragmentDirections.actionPlaceDetailFragmentToReviewsFragment(
                        reviews.toTypedArray(), position
                    ))
                }
            }
            if (itemDecorationCount == 0) {
                addItemDecoration(LinearSpaceItemDecoration(16, RecyclerView.HORIZONTAL))
            }
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

    private fun setUserReview(review: ContributionUserPlaceData) {
        if (review.review.isNullOrEmpty() && review.facilities.isEmpty()) {
            showEmptyUserReview()
            return
        }

        binding?.apply {
            llYourReview.visibility = View.VISIBLE
            btnAddReview.visibility = View.GONE
            tvYourReview.text = review.review
            if (review.review.isNullOrEmpty()) tvYourReview.visibility = View.GONE
            rvYourReviewFacilities.apply {
                adapter = SingleReviewFacilitiesAdapter().apply {
                    submitList(review.facilities)
                }
                if (itemDecorationCount == 0) {
                    addItemDecoration(LinearSpaceItemDecoration(16, RecyclerView.HORIZONTAL))
                }
            }
            if (review.is_moderated) {
                cdModeratedWarning.visibility = View.VISIBLE
                tvModeratedMessage.text = Html.fromHtml(getString(R.string.moderated_warning_message, review.moderation_reason))
            }
            else cdModeratedWarning.visibility = View.GONE

            btnEditReview.setOnClickListener {
                val action = PlaceDetailFragmentDirections.actionPlaceDetailFragmentToEditContributionFragment(review)
                findNavController().navigate(action)
            }

            btnDeleteReview.setOnClickListener {
                viewModel.deleteReview(review.place_id).observe(viewLifecycleOwner) {
                    if (it != null) {
                        when (it) {
                            is Resource.Error -> {
                                binding?.root?.showSnackbar(
                                    message = requireContext().translateErrorMessage(it.message),
                                    anchor = binding?.root?.rootView?.findViewById(R.id.bottom_nav_view)
                                )

                                binding?.apply {
                                    btnDeleteReview.isEnabled = true
                                    btnEditReview.isEnabled = true
                                }
                            }
                            is Resource.Loading -> {
                                binding?.apply {
                                    btnDeleteReview.isEnabled = false
                                    btnEditReview.isEnabled = false
                                }
                            }
                            is Resource.Success -> {
                                binding?.root?.showSnackbar(
                                    message = getString(R.string.review_deleted),
                                )

                                binding?.apply {
                                    btnDeleteReview.isEnabled = true
                                    btnEditReview.isEnabled = true
                                    llYourReview.visibility = View.GONE
                                    btnAddReview.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setFavorite(favorite: Boolean) {
        binding?.apply {
            if (favorite) {
                btnFavorite.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_bookmark_24)
                btnFavorite.contentDescription = getString(R.string.remove_from_favorite)

                btnFavorite.setOnClickListener {
                    viewModel.deleteFavoritePlace(place.id).observe(viewLifecycleOwner) {
                        if (it != null) {
                            when (it) {
                                is Resource.Error -> {
                                    binding?.root?.showSnackbar(
                                        message = requireContext().translateErrorMessage(it.message),
                                    )
                                    binding?.btnFavorite?.isEnabled = true
                                }
                                is Resource.Loading -> {
                                    binding?.btnFavorite?.isEnabled = false
                                }
                                is Resource.Success -> {
                                    binding?.btnFavorite?.isEnabled = true
                                    setFavorite(false)
                                }
                            }
                        }
                    }
                }
            } else {
                btnFavorite.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_bookmark_border_24)
                btnFavorite.contentDescription = getString(R.string.add_to_favorite)

                btnFavorite.setOnClickListener {
                    viewModel.addFavoritePlace(place.id).observe(viewLifecycleOwner) {
                        if (it != null) {
                            when (it) {
                                is Resource.Error -> {
                                    binding?.root?.showSnackbar(
                                        message = requireContext().translateErrorMessage(it.message),
                                    )
                                    binding?.btnFavorite?.isEnabled = true
                                }
                                is Resource.Loading -> {
                                    binding?.btnFavorite?.isEnabled = false
                                }
                                is Resource.Success -> {
                                    binding?.btnFavorite?.isEnabled = true
                                    setFavorite(true)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showEmptyUserReview() {
        binding?.apply {
            llYourReview.visibility = View.GONE
            btnAddReview.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        map = null
    }
}