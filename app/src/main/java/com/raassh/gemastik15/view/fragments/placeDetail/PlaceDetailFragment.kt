package com.raassh.gemastik15.view.fragments.placeDetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.raassh.gemastik15.R

class PlaceDetailFragment : Fragment() {

    companion object {
        fun newInstance() = PlaceDetailFragment()
    }

    private lateinit var viewModel: PlaceDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PlaceDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}