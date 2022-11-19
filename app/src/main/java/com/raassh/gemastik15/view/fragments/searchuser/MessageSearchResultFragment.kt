package com.raassh.gemastik15.view.fragments.searchuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.raassh.gemastik15.adapter.ChatListAdapter
import com.raassh.gemastik15.databinding.FragmentMessageSearchResultBinding
import com.raassh.gemastik15.view.fragments.chatlist.IChatListViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MessageSearchResultFragment : Fragment() {
    private val viewModel by sharedViewModel<SearchUserViewModel>()
    private var binding: FragmentMessageSearchResultBinding? = null

    private lateinit var chatAdapter: ChatListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessageSearchResultBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatAdapter = ChatListAdapter(viewModel as IChatListViewModel).apply {
            onItemClickListener = {
                val action = SearchUserFragmentDirections.actionSearchUserFragmentToChatFragment()
                action.chat = it
                findNavController().navigate(action)
            }
        }

        binding?.apply {
            rvChats.adapter = chatAdapter
        }

        viewModel.apply {
            chats.observe(viewLifecycleOwner) {
                if (it.isEmpty()) {
                    showEmpty(true)
                } else {
                    showEmpty(false)
                    chatAdapter.submitList(it)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()


    }

    private fun showEmpty(isEmpty: Boolean) {
        binding?.apply {
            if (isEmpty) {
                tvNoMessage.visibility = View.VISIBLE
                rvChats.visibility = View.GONE
            } else {
                tvNoMessage.visibility = View.GONE
                rvChats.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}