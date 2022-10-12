package com.raassh.gemastik15.view.fragments.AddContribution

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.raassh.gemastik15.R
import com.raassh.gemastik15.databinding.FragmentAddContributionBinding
import com.raassh.gemastik15.local.db.Facility
import com.raassh.gemastik15.utils.FacilityDataXmlParser
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.reflect.Field

class AddContributionFragment : Fragment() {
    private val viewModel by viewModel<AddContributionViewModel>()
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

        binding?.apply {
            btnBack.setOnClickListener {
                activity?.onBackPressed()
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

            isLoading.observe(viewLifecycleOwner) {
                binding?.apply {
                    if (it) {
                        btnFacilityReviewBad.isEnabled = false
                        btnFacilityReviewGood.isEnabled = false
                        btnFacilityReviewNone.isEnabled = false
                        btnFacilityReviewDontKnow.isEnabled = false

                        spnFacilityReview.visibility = View.VISIBLE
                        imgFacilityIcon.visibility = View.GONE
                        tvFacilityName.visibility = View.GONE
                        tvFacilityDescription.visibility = View.GONE
                    } else {
                        btnFacilityReviewBad.isEnabled = true
                        btnFacilityReviewGood.isEnabled = true
                        btnFacilityReviewNone.isEnabled = true
                        btnFacilityReviewDontKnow.isEnabled = true

                        spnFacilityReview.visibility = View.GONE
                        imgFacilityIcon.visibility = View.VISIBLE
                        tvFacilityName.visibility = View.VISIBLE
                        tvFacilityDescription.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    fun prepareFacilityData() {
        val stream = resources.openRawResource(R.raw.facility_data)
        val facilities: List<Facility> = FacilityDataXmlParser().parse(stream) ?: emptyList()

        viewModel.facilities.value = facilities
        viewModel.currentFacility.value = facilities[0]
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