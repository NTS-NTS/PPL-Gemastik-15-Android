package com.raassh.gemastik15.view.fragments.placeDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.raassh.gemastik15.databinding.FragmentPlaceDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaceDetailFragment : Fragment() {
    private val viewModel by viewModel<PlaceDetailViewModel>()
    private var binding: FragmentPlaceDetailBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaceDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            //
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