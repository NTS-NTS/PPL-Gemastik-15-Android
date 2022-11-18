package com.raassh.gemastik15.view.fragments.searchuser

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.navigation.fragment.findNavController
import com.raassh.gemastik15.R
import com.raassh.gemastik15.adapter.ChatListAdapter
import com.raassh.gemastik15.adapter.UserListAdapter
import com.raassh.gemastik15.databinding.FragmentSearchUserBinding
import com.raassh.gemastik15.utils.*
import com.raassh.gemastik15.view.fragments.chatlist.IChatListViewModel
import dev.chrisbanes.insetter.applyInsetter
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchUserFragment : Fragment() {
    private val viewModel by viewModel<SearchUserViewModel>()
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

        val chatAdapter = ChatListAdapter(viewModel as IChatListViewModel).apply {
            onItemClickListener = {
                Log.d("TAG", "onViewCreated: $it")
            }
        }

        val userAdapter = UserListAdapter().apply {
            onItemClickListener = {
                Log.d("TAG", "onViewCreated: $it")
            }
        }

        binding?.apply {
            root.applyInsetter { type(statusBars = true) { padding() } }

            rvChats.adapter = chatAdapter
            rvUsers.adapter = userAdapter

            etSearch.on(EditorInfo.IME_ACTION_DONE) {
                val username = etSearch.text.toString()
                if (username.isEmpty()) {
                    root.showSnackbar(
                        message = getString(R.string.empty_query),
                        anchor = binding?.root?.rootView?.findViewById(R.id.bottom_nav_view)
                    )
                    return@on
                }

                val action = SearchUserFragmentDirections.actionSearchUserFragmentSelf(username)
                findNavController().navigate(action)
            }
        }

        viewModel.apply {
            setQuery(query)

            users.observe(viewLifecycleOwner) {
                when(it) {
                    is Resource.Loading -> {
                        Log.d("TAG", "onViewCreated: Loading")
                    }
                    is Resource.Success -> {
                        userAdapter.submitList(it.data)
                    }
                    is Resource.Error -> {
                        Log.d("TAG", "onViewCreated: ${it.message}")
                    }
                }
            }

            chats.observe(viewLifecycleOwner) {
                chatAdapter.submitList(it)
            }
        }
    }
}