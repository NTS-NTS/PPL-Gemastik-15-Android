package com.raassh.gemastik15.view.activity.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.raassh.gemastik15.R
import com.raassh.gemastik15.databinding.ActivityMainBinding
import com.raassh.gemastik15.utils.setTheme
import com.raassh.gemastik15.utils.showSnackbar
import com.raassh.gemastik15.view.activity.dashboard.DashboardActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModel<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var needRelogin = intent.getBooleanExtra(NEED_RELOGIN, false)

        if (needRelogin) {
            binding.root.showSnackbar(getString(R.string.invalid_token))
            viewModel.clearToken()
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        viewModel.apply {
            token.observe(this@MainActivity) {
                if (!it.isNullOrBlank() && !needRelogin) {
                    startActivity(Intent(this@MainActivity, DashboardActivity::class.java))
                    finish()
                } else if (it.isNullOrBlank()) {
                    needRelogin = false
                }
            }

            theme.observe(this@MainActivity) {
                if (it != null) {
                    setTheme(it)
                }
            }
        }
    }

    companion object {
        const val NEED_RELOGIN = "NEED_RELOGIN"
    }
}