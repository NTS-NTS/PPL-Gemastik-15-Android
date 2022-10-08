package com.raassh.gemastik15.view.activity.dashboard

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.raassh.gemastik15.R
import com.raassh.gemastik15.databinding.ActivityDashboardBinding
import com.raassh.gemastik15.utils.checkPermission
import com.raassh.gemastik15.utils.showSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private val viewModel by viewModel<DashboardViewModel>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                getMyLastLocation()
            }
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                // Only approximate location access granted.
                getMyLastLocation()
            }
            else -> {
                // No location access granted.
                Log.d("TAG", "requestPermissionLauncher: Permissions not granted")
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    viewModel.setLocation(location.latitude, location.longitude)
                } else {
                    Log.d("TAG", "getMyLastLocation: Location not found")
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView = binding.bottomNavView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        val navController = navHostFragment.navController

//        val appBarConfiguration = AppBarConfiguration.Builder(
//            R.id.navigation_discover
//        ).build()
//        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

        // temporary
        navView.setOnItemSelectedListener {
            if (it.itemId != R.id.navigation_discover) {
                binding.root.showSnackbar(getString(R.string.feature_not_available))
            }

            false
        }

        onBackPressedDispatcher.addCallback {
            if (!navController.navigateUp()) {
                finish()
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getMyLastLocation()
    }
}