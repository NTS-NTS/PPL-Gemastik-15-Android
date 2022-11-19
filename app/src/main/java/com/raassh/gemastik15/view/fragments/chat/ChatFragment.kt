package com.raassh.gemastik15.view.fragments.chat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.raassh.gemastik15.R
import com.raassh.gemastik15.adapter.ChatAdapter
import com.raassh.gemastik15.databinding.FragmentChatBinding
import com.raassh.gemastik15.utils.Resource
import com.raassh.gemastik15.utils.showSnackbar
import com.raassh.gemastik15.utils.translateErrorMessage
import com.raassh.gemastik15.view.activity.dashboard.DashboardViewModel
import dev.chrisbanes.insetter.applyInsetter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatFragment : Fragment(), PopupMenu.OnMenuItemClickListener{
    private val viewModel by viewModel<ChatViewModel>()
    private val sharedViewModel by sharedViewModel<DashboardViewModel>()
    private var binding: FragmentChatBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chat = ChatFragmentArgs.fromBundle(requireArguments()).chat
        val receiver = ChatFragmentArgs.fromBundle(requireArguments()).receiver

        val adapter = ChatAdapter()

        sharedViewModel.getUsername().observe(viewLifecycleOwner) {
            adapter.username = it ?: ""
            viewModel.username = it ?: ""
        }

        binding?.apply {
            root.applyInsetter { type(statusBars = true, navigationBars = true) { padding() } }

            tvUsername.text = chat?.users ?: receiver

            rvChat.adapter = adapter

            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnMenu.setOnClickListener {
                showMenu(it)
            }

            tvUsername.setOnClickListener {
                val action = ChatFragmentDirections.actionChatFragmentToUserProfileFragment(chat?.id)
                findNavController().navigate(action)
            }

            etMessage.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // intentionally left blank
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // intentionally left blank
                }

                override fun afterTextChanged(p0: Editable?) {
                    btnSend.isEnabled = p0.toString().isNotBlank()
                }

            })

            btnSend.setOnClickListener {
                val message = etMessage.text.toString()

                btnSend.isEnabled = false
                etMessage.isEnabled = false

                if (chat != null) {
                    viewModel.sendMessage(message).observe(viewLifecycleOwner) {
                        when (it) {
                            is Resource.Success -> {
                                etMessage.text?.clear()
                                etMessage.isEnabled = true
                                btnSend.isEnabled = true
                            }
                            is Resource.Error -> {
                                root.showSnackbar(requireContext().translateErrorMessage(it.message))
                            }
                            is Resource.Loading -> {
                                // intentionally left blank
                            }
                        }
                    }
                } else {
                    viewModel.startChat(receiver ?: "", message).observe(viewLifecycleOwner) {
                        when (it) {
                            is Resource.Success -> {
                                etMessage.text?.clear()
                                etMessage.isEnabled = true
                                btnSend.isEnabled = true

                                viewModel.setChatId(it.data?.chatId ?: "")
                            }
                            is Resource.Error -> {
                                root.showSnackbar(requireContext().translateErrorMessage(it.message))
                            }
                            is Resource.Loading -> {
                                // intentionally left blank
                            }
                        }
                    }
                }
            }
        }

        viewModel.apply {
            setChatId(chat?.id ?: "")

            messages.observe(viewLifecycleOwner) {
                adapter.submitList(it)
                binding?.rvChat?.scrollToPosition(it.size - 1)
            }
        }
    }

    private fun showMenu(v: View) {
        PopupMenu(context, v).apply {
            setOnMenuItemClickListener(this@ChatFragment)
            inflate(R.menu.chat_menu)
            try {
                val field = PopupMenu::class.java.getDeclaredField("mPopup")
                field.isAccessible = true
                val menuHelper = field.get(this)
                val classPopupHelper = Class.forName(menuHelper.javaClass.name)
                val setForceIcons = classPopupHelper.getMethod("setForceShowIcon", Boolean::class.java)
                setForceIcons.invoke(menuHelper, true)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                show()
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.report_user -> {
                val action = ChatFragmentDirections.actionChatFragmentToReportUserFragment(
//                    TODO: user id
                )
                findNavController().navigate(action)
                true
            }
            else -> false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}