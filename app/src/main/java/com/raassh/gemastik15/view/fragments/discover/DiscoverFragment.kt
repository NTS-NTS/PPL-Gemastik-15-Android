package com.raassh.gemastik15.view.fragments.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.raassh.gemastik15.R
import com.raassh.gemastik15.adapter.PlaceAdapter
import com.raassh.gemastik15.databinding.FragmentDiscoverBinding
import com.raassh.gemastik15.local.db.PlaceEntity
import com.raassh.gemastik15.utils.getCheckedFacilities
import com.raassh.gemastik15.utils.on
import com.raassh.gemastik15.utils.showSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class DiscoverFragment : Fragment() {
    private val viewModel by viewModel<DiscoverViewModel>()
    private var binding: FragmentDiscoverBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            btnSearch.setOnClickListener {
                val facilities = mutableListOf<String>()
                getCheckedFacilities(glFacilities, facilities)

                if (facilities.isEmpty()) {
                    root.showSnackbar(getString(R.string.no_facilities_chosen))
                    return@setOnClickListener
                }

                val action = DiscoverFragmentDirections
                    .actionNavigationDiscoverToSearchByFacilityFragment(facilities.toTypedArray())
                findNavController().navigate(action)
            }

            btnAll.setOnClickListener {
                findNavController().navigate(DiscoverFragmentDirections.actionNavigationDiscoverToSearchFacilityOptionFragment())
            }

            etSearch.on(EditorInfo.IME_ACTION_DONE) {
                val action =
                    DiscoverFragmentDirections.actionNavigationDiscoverToSearchResultFragment()
                action.query = etSearch.text.toString()
                findNavController().navigate(action)
            }

            rvRecent.adapter = PlaceAdapter().apply {
                submitList(
                    listOf(
                        PlaceEntity(
                            id = "1",
                            name = "Instiut Teknologi Sepuluh Nopember",
                            type = "Universitas",
                            image = "https://akcdn.detik.net.id/community/media/visual/2020/11/20/ilustrasi-its-institut-teknologi-sepuluh-nopember.jpeg?w=700&q=90",
                            distance = 0.1,
                            facilities = "Toilet,Lift,Ramp"
                        )
                    )
                )
            }
        }

        viewModel.apply {
            //
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}