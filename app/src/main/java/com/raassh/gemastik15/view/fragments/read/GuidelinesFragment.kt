package com.raassh.gemastik15.view.fragments.read

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.raassh.gemastik15.R
import com.raassh.gemastik15.adapter.ArticleAdapter
import com.raassh.gemastik15.databinding.FragmentArticlesBinding
import com.raassh.gemastik15.databinding.FragmentGuidelinesBinding
import com.raassh.gemastik15.utils.Resource
import com.raassh.gemastik15.utils.showSnackbar
import com.raassh.gemastik15.utils.translateErrorMessage
import org.koin.androidx.viewmodel.ext.android.viewModel

class GuidelinesFragment : Fragment() {
    private val viewModel by viewModel<ReadViewModel>()
    private var binding: FragmentGuidelinesBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGuidelinesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val articlesAdapter = ArticleAdapter().apply {
            onItemClickListener = { article ->
                val action =
                    ReadFragmentDirections.actionNavigationReadToWebViewerFragment(
                        article.url
                    )
                findNavController().navigate(action)
            }
        }

        binding?.apply {
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL).let {
                rvGuidelines.addItemDecoration(it)
            }

            rvGuidelines.adapter = articlesAdapter
        }

        viewModel.guidelines.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is Resource.Loading -> {
                        showLoading(true)
                    }
                    is Resource.Success -> {
                        showLoading(false)

                        if (it.data?.isNotEmpty() == true) articlesAdapter.submitList(it.data)
                        else showEmpty()
                    }
                    is Resource.Error -> {
                        showLoading(false, error = true)
                        showEmpty()

                        binding?.root?.showSnackbar(
                            message = requireContext().translateErrorMessage(it.message),
                            anchor = binding?.root?.rootView?.rootView?.findViewById(R.id.bottom_nav_view)
                        )
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean, error: Boolean = false) {
        binding?.apply {
            if (isLoading) {
                pbLoading.visibility = View.VISIBLE
                rvGuidelines.visibility = View.GONE
            } else {
                pbLoading.visibility = View.GONE
                rvGuidelines.visibility = View.VISIBLE
            }
        }
    }

    private fun showEmpty() {
        binding?.apply {
            pbLoading.visibility = View.GONE
            rvGuidelines.visibility = View.GONE
            tvNoGuidelines.visibility = View.VISIBLE
        }
    }
}