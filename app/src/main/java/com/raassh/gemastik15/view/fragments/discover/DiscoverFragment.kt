package com.raassh.gemastik15.view.fragments.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.fragment.app.Fragment
import com.raassh.gemastik15.databinding.FragmentDiscoverBinding
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
            setupFacilities(glFacilities)
        }

        viewModel.apply {
            //
        }
    }

    private fun setupFacilities(container: ViewGroup) {
        val childCount = container.childCount
        for (i in 0 until childCount) {
            val child = container.getChildAt(i)
            if (child is CheckedTextView) {
                child.setOnClickListener {
                    child.toggle()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}