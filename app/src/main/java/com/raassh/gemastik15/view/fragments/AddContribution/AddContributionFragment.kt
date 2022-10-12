package com.raassh.gemastik15.view.fragments.AddContribution

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.raassh.gemastik15.R

class AddContributionFragment : Fragment() {

    companion object {
        fun newInstance() = AddContributionFragment()
    }

    private lateinit var viewModel: AddContributionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_contribution, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddContributionViewModel::class.java)
        // TODO: Use the ViewModel
    }

}