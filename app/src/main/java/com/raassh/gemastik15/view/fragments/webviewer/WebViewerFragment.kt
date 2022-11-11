package com.raassh.gemastik15.view.fragments.webviewer

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.raassh.gemastik15.databinding.FragmentWebViewerBinding
import dev.chrisbanes.insetter.applyInsetter


class WebViewerFragment : Fragment() {
    private var binding: FragmentWebViewerBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWebViewerBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = WebViewerFragmentArgs.fromBundle(requireArguments()).url

        binding?.apply {
            root.applyInsetter { type(statusBars = true, navigationBars = true) { padding() } }

            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            webView.apply {
                webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView, progress: Int)  {

                        progressBar.progress = progress

                        if (progress == 100) {
                            progressBar.visibility = View.GONE
                            visibility = View.VISIBLE
                        }
                    }
                }

                loadUrl(url)
            }
        }
    }
}