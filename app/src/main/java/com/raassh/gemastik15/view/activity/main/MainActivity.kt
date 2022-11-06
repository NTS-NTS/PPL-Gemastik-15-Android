package com.raassh.gemastik15.view.activity.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import com.raassh.gemastik15.R
import com.raassh.gemastik15.databinding.ActivityMainBinding
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

        val needRelogin = intent.getBooleanExtra(NEED_RELOGIN, false)

        if (needRelogin) {
            binding.root.showSnackbar(getString(R.string.invalid_token))
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        viewModel.apply {
            token.observe(this@MainActivity) {
                if (!it.isNullOrBlank() && !needRelogin) {
                    startActivity(Intent(this@MainActivity, DashboardActivity::class.java))
                    finish()
                }
            }

            theme.observe(this@MainActivity) {
                if (it != null) {
                    setTheme(it)
                }
            }
        }
    }

    private fun setTheme(theme: String) {
        when(theme) {
            "MODE_NIGHT_FOLLOW_SYSTEM" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            "MODE_NIGHT_NO" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            "MODE_NIGHT_YES" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            "MODE_NIGHT_AUTO_BATTERY" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
            }
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }

    companion object {
        const val NEED_RELOGIN = "NEED_RELOGIN"
    }
}