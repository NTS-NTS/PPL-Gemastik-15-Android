package com.raassh.gemastik15.view.fragments.chatlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.raassh.gemastik15.R
import com.raassh.gemastik15.adapter.ChatListAdapter
import com.raassh.gemastik15.databinding.FragmentChatListBinding
import com.raassh.gemastik15.utils.*
import dev.chrisbanes.insetter.applyInsetter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatListFragment : Fragment() {
    private val viewModel by viewModel<ChatListViewModel>()
    private var binding: FragmentChatListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ChatListAdapter(viewModel as IChatListViewModel).apply {
            onItemClickListener = {
                Log.d("TAG", "onViewCreated: $it")
            }
        }

        binding?.apply {
            root.applyInsetter { type(statusBars = true) { padding() } }

            rvChats.adapter = adapter

            etSearch.on(EditorInfo.IME_ACTION_DONE) {
                val username = etSearch.text.toString()
                if (username.isEmpty()) {
                    root.showSnackbar(
                        message = getString(R.string.empty_query),
                        anchor = binding?.root?.rootView?.findViewById(R.id.bottom_nav_view)
                    )
                    return@on
                }

                val action = ChatListFragmentDirections.actionChatListFragmentToSearchUserFragment(username)
                findNavController().navigate(action)
            }
        }

        viewModel.chats.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

}