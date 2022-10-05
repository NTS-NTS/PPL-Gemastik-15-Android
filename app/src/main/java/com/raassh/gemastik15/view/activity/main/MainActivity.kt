package com.raassh.gemastik15.view.activity.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.raassh.gemastik15.databinding.ActivityMainBinding
import com.raassh.gemastik15.view.activity.dashboard.DashboardActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModel<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.apply {
            getToken().observe(this@MainActivity) {
                if (!it.isNullOrBlank()) {
                    startActivity(Intent(this@MainActivity, DashboardActivity::class.java))
                    finish()
                }
            }
        }
    }
}