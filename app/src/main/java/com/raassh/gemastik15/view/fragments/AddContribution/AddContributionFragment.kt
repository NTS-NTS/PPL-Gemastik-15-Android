package com.raassh.gemastik15.view.fragments.AddContribution

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.auth0.android.jwt.JWT
import com.raassh.gemastik15.R
import com.raassh.gemastik15.databinding.FragmentAddContributionBinding
import com.raassh.gemastik15.local.db.Facility
import com.raassh.gemastik15.utils.FacilityDataXmlParser
import com.raassh.gemastik15.utils.Resource
import com.raassh.gemastik15.utils.showSnackbar
import com.raassh.gemastik15.view.activity.dashboard.DashboardViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.reflect.Field

class AddContributionFragment : Fragment() {
    private val viewModel by viewModel<AddContributionViewModel>()
    private val sharedViewModel by viewModel<DashboardViewModel>()
    private var binding: FragmentAddContributionBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddContributionBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareFacilityData()

        val placeId = AddContributionFragmentArgs.fromBundle(requireArguments()).placeId
        val jwt = JWT(sharedViewModel.getToken().value.toString())
        val userId = jwt.id

        binding?.apply {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnFacilityReviewDone.setOnClickListener {
                findNavController().navigateUp()
            }

            btnFacilityReviewGood.setOnClickListener {
                if (userId != null) {
                    trySubmitContribution(userId, placeId, 2)
                }
            }

            btnFacilityReviewBad.setOnClickListener {
                if (userId != null) {
                    trySubmitContribution(userId, placeId, 1)
                }
            }

            btnFacilityReviewNone.setOnClickListener {
                if (userId != null) {
                    trySubmitContribution(userId, placeId, 0)
                }
            }

            btnFacilityReviewDontKnow.setOnClickListener {
                viewModel.nextFacility()
            }
        }

        viewModel.apply {
            currentFacility.observe(viewLifecycleOwner) {
                binding?.apply {
                    tvFacilityName.text = getString(getResId(it.name, R.string::class.java))
                    tvFacilityDescription.text = getString(getResId(it.description, R.string::class.java))
                    imgFacilityIcon.setImageResource(getResId(it.icon, R.drawable::class.java))
                }
            }

            isDone.observe(viewLifecycleOwner) {
                binding?.apply {
                    if (it) {
                        llFacilityReviewDone.visibility = View.VISIBLE
                        llFacilities.visibility = View.GONE
                    } else {
                        llFacilityReviewDone.visibility = View.GONE
                        llFacilities.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun prepareFacilityData() {
        val istream = resources.openRawResource(R.raw.facility_data)
        val facilities: List<Facility> = FacilityDataXmlParser().parse(istream)

        viewModel.facilities.value = facilities
        viewModel.currentFacility.value = facilities[0]
    }

    private fun trySubmitContribution(userId: String, placeId: String, rating: Int) {
        viewModel.submitContribution(userId, placeId, rating).observe(viewLifecycleOwner) { response ->
            if (response != null) {
                when (response) {
                    is Resource.Loading -> {
                        setLoading(true)
                    }
                    is Resource.Success -> {
                        viewModel.nextFacility()
                        setLoading(false)
                    }
                    is Resource.Error -> {
                        setLoading(false)

                        binding?.root?.showSnackbar(
                            response.message ?: getString(R.string.unknown_error)
                        )
                    }
                }
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding?.apply {
            btnFacilityReviewBad.isEnabled = !isLoading
            btnFacilityReviewGood.isEnabled = !isLoading
            btnFacilityReviewNone.isEnabled = !isLoading
            btnFacilityReviewDontKnow.isEnabled = !isLoading

            spnFacilityReview.visibility = if (isLoading) View.VISIBLE else View.GONE
            imgFacilityIcon.visibility = if (isLoading) View.GONE else View.VISIBLE
            tvFacilityName.visibility = if (isLoading) View.GONE else View.VISIBLE
            tvFacilityDescription.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }

    private fun getResId(resName: String, c: Class<*>): Int {
        return try {
            val idField: Field = c.getDeclaredField(resName)
            idField.getInt(idField)
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }
}