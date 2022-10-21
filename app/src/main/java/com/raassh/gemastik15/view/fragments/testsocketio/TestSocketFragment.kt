package com.raassh.gemastik15.view.fragments.testsocketio

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.raassh.gemastik15.databinding.FragmentTestSocketBinding
import io.socket.client.IO
import io.socket.client.Socket

class TestSocketFragment : Fragment() {
    private var binding: FragmentTestSocketBinding? = null
    private var socket: Socket? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTestSocketBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            socket = IO.socket("http://10.0.2.2:8000")
            socket?.let {
                Log.d("TAG", "nyambung")
//                Log.d("TAG", it.id())
                it.connect()
                it.on("my_response") { args ->
                    for (arg in args) {
                        Log.d("TAG", arg.toString())
                    }
                }
            }


        } catch (e: Exception) {
            Log.e("TAG", "Failed to connect", e)
        }



        binding?.btnSend?.setOnClickListener {
            val event = binding?.etMessageEvent?.text.toString()
            val message = binding?.etMessage?.text.toString()
            if (event.isNotEmpty() && message.isNotEmpty()) {
                socket?.emit(event, message)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}