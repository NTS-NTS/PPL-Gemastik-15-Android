package com.raassh.gemastik15.view.fragments.webviewer

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.raassh.gemastik15.R
import com.raassh.gemastik15.databinding.FragmentWebViewerBinding
import com.raassh.gemastik15.utils.getUrlDomain
import dev.chrisbanes.insetter.applyInsetter


class WebViewerFragment : Fragment(), PopupMenu.OnMenuItemClickListener {
    private var binding: FragmentWebViewerBinding? = null
    private var currentUrl: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWebViewerBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentUrl = WebViewerFragmentArgs.fromBundle(requireArguments()).url

        binding?.apply {
            root.applyInsetter { type(statusBars = true, navigationBars = true) { padding() } }

            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnMenu.setOnClickListener {
                showMenu(it)
            }

            webView.apply {
                webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView, progress: Int)  {

                        progressBar.progress = progress

                        if (progress == 100) {
                            progressBar.visibility = View.GONE
                            divider.visibility = View.VISIBLE
                            visibility = View.VISIBLE
                        } else {
                            progressBar.visibility = View.VISIBLE
                            divider.visibility = View.GONE
                        }
                    }

                    override fun onReceivedTitle(view: WebView, title: String) {
                        super.onReceivedTitle(view, title)
                        tvTitle.text = title
                    }
                }

                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        if (url != null) {
                            tvUrl.text = url.getUrlDomain()
                            currentUrl = url
                        }
                    }
                }

                loadUrl(currentUrl)
            }
        }
    }

    private fun showMenu(v: View) {
        PopupMenu(context, v).apply {
            setOnMenuItemClickListener(this@WebViewerFragment)
            inflate(R.menu.web_viewer_menu)
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
            R.id.share -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, currentUrl)
                    type = "text/plain"
                }
                val title: String = getString(R.string.share)
                val chooser: Intent = Intent.createChooser(sendIntent, title)
                if (sendIntent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(chooser)
                }
                true
            }
            R.id.open_in_browser -> {
                val webpage: Uri = Uri.parse(currentUrl)
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                startActivity(intent)
                true
            }
            else -> false
        }
    }
}