package com.raassh.gemastik15.view.fragments.searchuser

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.raassh.gemastik15.R
import com.raassh.gemastik15.adapter.ChatListAdapter
import com.raassh.gemastik15.adapter.ReadPagerAdapter
import com.raassh.gemastik15.adapter.SearchUserPagerAdapter
import com.raassh.gemastik15.adapter.UserListAdapter
import com.raassh.gemastik15.databinding.FragmentSearchUserBinding
import com.raassh.gemastik15.utils.*
import com.raassh.gemastik15.view.fragments.chatlist.IChatListViewModel
import dev.chrisbanes.insetter.applyInsetter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchUserFragment : Fragment() {
    private val viewModel by sharedViewModel<SearchUserViewModel>()
    private var binding: FragmentSearchUserBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchUserBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val query = SearchUserFragmentArgs.fromBundle(requireArguments()).query

        binding?.apply {
            root.applyInsetter { type(statusBars = true, navigationBars = true) { padding() } }
            etSearch.setText(query)
            etSearch.on(EditorInfo.IME_ACTION_DONE) {
                val username = etSearch.text.toString()
                if (username.isEmpty()) {
                    root.showSnackbar(
                        message = getString(R.string.empty_query),
                    )
                    return@on
                }

                val action = SearchUserFragmentDirections.actionSearchUserFragmentSelf(username)
                findNavController().navigate(action)
            }

            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            viewModel.apply {
                setQuery(query)


                users.observe(viewLifecycleOwner) {
                    Log.d("TAG", "onViewCreated: $it")
                    when(it) {
                        is Resource.Loading -> {
                        }
                        is Resource.Success -> {
                            view
                        }
                        is Resource.Error -> {

                            binding?.root?.showSnackbar(
                                message = requireContext().translateErrorMessage(it.message),
                            )
                        }
                    }
                }

                val q = viewModel.query.value
                Log.d("query", q.toString())

                viewPager.adapter = SearchUserPagerAdapter(this@SearchUserFragment)

                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    val q = viewModel.query.value
                    Log.d("query", q.toString())
                    when (position) {
                        0 -> {
                            tab.text = getString(R.string.message)
                        }
                        1 -> {
                            tab.text = getString(R.string.user)
                        }
                    }
                }.attach()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}