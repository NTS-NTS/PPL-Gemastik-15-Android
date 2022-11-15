package com.raassh.gemastik15.view.fragments.editcontribution

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.raassh.gemastik15.R
import com.raassh.gemastik15.adapter.EditReviewFacilitiesAdapter
import com.raassh.gemastik15.databinding.FragmentDiscoverBinding
import com.raassh.gemastik15.databinding.FragmentEditContributionBinding
import com.raassh.gemastik15.local.db.Facility
import com.raassh.gemastik15.utils.FacilityDataXmlParser
import com.raassh.gemastik15.utils.Resource
import com.raassh.gemastik15.utils.showSnackbar
import com.raassh.gemastik15.utils.translateErrorMessage
import com.raassh.gemastik15.view.activity.dashboard.DashboardViewModel
import com.raassh.gemastik15.view.fragments.discover.DiscoverViewModel
import dev.chrisbanes.insetter.applyInsetter
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditContributionFragment : Fragment() {
    private val viewModel by viewModel<EditContributionViewModel>()
    private val sharedViewModel by viewModel<DashboardViewModel>()
    private var binding: FragmentEditContributionBinding? = null

    private var token: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditContributionBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val review = EditContributionFragmentArgs.fromBundle(requireArguments()).review
        val istream = resources.openRawResource(R.raw.facility_data)
        val facilities: List<Facility> = FacilityDataXmlParser().parse(istream)

        binding?.apply {
            root.applyInsetter { type(statusBars = true, navigationBars = true) { padding() } }

            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            rvYourReviewFacilities.apply {
                adapter = EditReviewFacilitiesAdapter(review.facilities).apply {
                    submitList(facilities)

                    onButtonCheckedListener = { facility, checkedId, isChecked ->
                        if (isChecked) {
                            when (checkedId) {
                                R.id.btn_facility_review_good -> {
                                    viewModel.addFacilityReview(facility.name, 2)
                                }
                                R.id.btn_facility_review_bad -> {
                                    viewModel.addFacilityReview(facility.name, 1)
                                }
                                R.id.btn_facility_review_dont_know -> {
                                    viewModel.addFacilityReview(facility.name, 0)
                                }
                            }
                            viewModel.updateChange(review, etReview.text.toString())
                        }
                    }
                }
            }

            etReview.setText(review.review)

            etReview.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel.updateChange(review, p0.toString())
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })

            btnSendContribution.isEnabled = false

            btnSendContribution.setOnClickListener {
                showSendLoading(true)
                if (token != null) {
                    viewModel.submitReview(token!!, review.place_id, etReview.text.toString()).observe(viewLifecycleOwner) {
                        if (it != null) {
                            when (it) {
                                is Resource.Success -> {
                                    showSendLoading(false)
                                    binding?.root?.showSnackbar(getString(R.string.review_sent))
                                    findNavController().navigateUp()
                                }
                                is Resource.Error -> {
                                    showSendLoading(false)
                                    binding?.root?.showSnackbar(
                                        requireContext().translateErrorMessage(it.message)
                                    )
                                }
                                is Resource.Loading -> {
                                    showSendLoading(true)
                                }
                            }
                        }
                    }
                }
            }

            viewModel.isChanged.observe(viewLifecycleOwner) {
                btnSendContribution.isEnabled = it
            }
        }

        sharedViewModel.apply {
            getToken().observe(viewLifecycleOwner) {
                if (!it.isNullOrEmpty()) {
                    token = it
                }
            }
        }
    }

    private fun showSendLoading(isLoading: Boolean) {
        binding?.apply {
            if (isLoading) {
                btnSendContribution.isEnabled = false
                llReview.visibility = View.GONE
                llReviewSending.visibility = View.VISIBLE
            } else {
                btnSendContribution.isEnabled = true
                llReview.visibility = View.VISIBLE
                llReviewSending.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}