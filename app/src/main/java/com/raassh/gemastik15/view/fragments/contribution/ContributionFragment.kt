package com.raassh.gemastik15.view.fragments.contribution

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.raassh.gemastik15.R

class ContributionFragment : Fragment() {

    companion object {
        fun newInstance() = ContributionFragment()
    }

    private lateinit var viewModel: ContributionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contribution, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ContributionViewModel::class.java)
        // TODO: Use the ViewModel
    }

}