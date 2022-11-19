package com.raassh.gemastik15.view.fragments.chat

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
import com.auth0.android.jwt.JWT
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

        val receiver = ChatFragmentArgs.fromBundle(requireArguments()).receiver

        val chat = ChatFragmentArgs.fromBundle(requireArguments()).chat ?: viewModel.getChatId(receiver ?: "")

        val adapter = ChatAdapter()

        sharedViewModel.getToken().observe(viewLifecycleOwner) {
            if (!it.isNullOrBlank()) {
                val jwt = JWT(it)
                val id = jwt.getClaim("id").asString() ?: return@observe

                adapter.user_id = id
                viewModel.userId = id
            }
        }

        binding?.apply {
            root.applyInsetter { type(statusBars = true, navigationBars = true) { padding() } }

            rvChat.adapter = adapter

            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnMenu.setOnClickListener {
                showMenu(it)
            }

            tvUsername.setOnClickListener {
                if (chat != null) {
                    val action = ChatFragmentDirections.actionChatFragmentToUserProfileFragment(chat.users)
                    findNavController().navigate(action)
                } else {
                    val action = ChatFragmentDirections.actionChatFragmentToUserProfileFragment(receiver ?: "")
                    findNavController().navigate(action)
                }
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
                                etMessage.isEnabled = true
                                btnSend.isEnabled = true
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
                                etMessage.isEnabled = true
                                btnSend.isEnabled = true
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
            setReceiver(chat?.users ?: receiver ?: "")

            messages.observe(viewLifecycleOwner) {
                adapter.submitList(it)
                binding?.rvChat?.scrollToPosition(it.size - 1)
            }

            username.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Success -> {
                        binding?.tvUsername?.text = it.data
                    }
                    else -> {
                        // intentionally left blank
                    }
                }
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
                    viewModel.getReceiver()
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