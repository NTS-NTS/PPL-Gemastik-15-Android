package com.raassh.gemastik15.view.fragments.read

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.raassh.gemastik15.R
import com.raassh.gemastik15.adapter.ArticleAdapter
import com.raassh.gemastik15.databinding.FragmentNewsBinding
import com.raassh.gemastik15.utils.Resource
import com.raassh.gemastik15.utils.showSnackbar
import com.raassh.gemastik15.utils.translateErrorMessage
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class NewsFragment : Fragment() {
    private val viewModel by sharedViewModel<ReadViewModel>()
    private var binding: FragmentNewsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newsAdapter = ArticleAdapter().apply {
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
                rvNews.addItemDecoration(it)
            }

            rvNews.adapter = newsAdapter
        }

        viewModel.news.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is Resource.Loading -> {
                        showLoading(true)
                    }
                    is Resource.Success -> {
                        showLoading(false)

                        if (it.data?.isNotEmpty() == true) newsAdapter.submitList(it.data)
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
                rvNews.visibility = View.GONE
            } else {
                pbLoading.visibility = View.GONE
                rvNews.visibility = View.VISIBLE
            }
        }
    }

    private fun showEmpty() {
        binding?.apply {
            pbLoading.visibility = View.GONE
            rvNews.visibility = View.GONE
            tvNoNews.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}