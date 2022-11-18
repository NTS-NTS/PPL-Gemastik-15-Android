package com.raassh.gemastik15.view.fragments.searchuser

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.raassh.gemastik15.R
import com.raassh.gemastik15.adapter.ArticleAdapter
import com.raassh.gemastik15.adapter.ChatListAdapter
import com.raassh.gemastik15.databinding.FragmentMessageSearchResultBinding
import com.raassh.gemastik15.databinding.FragmentNewsBinding
import com.raassh.gemastik15.utils.Resource
import com.raassh.gemastik15.utils.showSnackbar
import com.raassh.gemastik15.utils.translateErrorMessage
import com.raassh.gemastik15.view.fragments.chatlist.IChatListViewModel
import com.raassh.gemastik15.view.fragments.read.ReadFragmentDirections
import com.raassh.gemastik15.view.fragments.read.ReadViewModel
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
                Log.d("TAG", "onViewCreated: $it")
            }
        }

        binding?.apply {
            rvChats.adapter = chatAdapter
        }


    }

    override fun onResume() {
        super.onResume()

        viewModel.apply {
            Log.d("TAG", "onViewCreated: ")
            val q = viewModel.query.value
            Log.d("query", q.toString())
            chats.observe(viewLifecycleOwner) {
                Log.d("TAG", "onViewCreated: $it")
                if (it.isEmpty()) {
                    showEmpty(true)
                } else {
                    showEmpty(false)
                    chatAdapter.submitList(it)
                }
            }
        }
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