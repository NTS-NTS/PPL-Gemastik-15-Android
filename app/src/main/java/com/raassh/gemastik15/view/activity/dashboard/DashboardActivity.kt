package com.raassh.gemastik15.view.activity.dashboard

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.activity.addCallback
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.*
import com.raassh.gemastik15.R
import com.raassh.gemastik15.databinding.ActivityDashboardBinding
import com.raassh.gemastik15.utils.checkPermission
import com.raassh.gemastik15.utils.showSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private val viewModel by viewModel<DashboardViewModel>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var isTracking = false

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                createLocationRequest()
            }
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                createLocationRequest()
            }
            else -> {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.permission_denied_warning))
                    .setMessage(getString(R.string.permissions_denied_warning_message))
                    .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                        dialog.dismiss()

                        val uri = Uri.fromParts("package", packageName, null)
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = uri
                        }
                        startActivity(intent)

                        createLocationRequest()
                    }
                    .setNegativeButton(getString(R.string.no)) { _, _ ->
                        finish()
                    }
                    .show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView = binding.bottomNavView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        val navController = navHostFragment.navController

        navView.setupWithNavController(navController)

        // might need to refactor this later
        navView.setOnItemSelectedListener {
            if (it.itemId != R.id.discover_nav
                && it.itemId != R.id.contribute_nav) {
                binding.root.showSnackbar(getString(R.string.feature_not_available))
                return@setOnItemSelectedListener false
            }

            navController.navigate(it.itemId)

            true
        }

        onBackPressedDispatcher.addCallback {
            if (!navController.navigateUp()) {
                finish()
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.locations.last()
                viewModel.setLocation(location.latitude, location.longitude)
            }
        }
        createLocationRequest()

        viewModel.location.observe(this) {
            Log.d("TAG", "onCreate: $it")
        }
    }

    private val resolutionLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    startLocationUpdates()
                }
                RESULT_CANCELED -> {
                    binding.root.showSnackbar(getString(R.string.turn_on_gps))
                }
            }
        }

    private fun createLocationRequest() {
        if (!checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) ||
            !checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .build()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        client.checkLocationSettings(builder.build())
            .addOnSuccessListener {
                startLocationUpdates()
            }
            .addOnFailureListener { exception ->
                if (exception is ApiException) {
                    when (exception.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            try {
                                val resolution = exception.status.resolution!!

                                resolutionLauncher.launch(
                                    IntentSenderRequest.Builder(resolution)
                                        .build()
                                )
                            } catch (exc: Exception) {
                                Log.d("TAG", "createLocationRequest: ${exc}")
                                binding.root.showSnackbar(exc.message ?: getString(R.string.unknown_error))
                            }
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            binding.root.showSnackbar(getString(R.string.turn_on_gps))
                        }
                    }
                }
            }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        isTracking = true
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (exception: SecurityException) {
            isTracking = false
            Log.e("TAG", "Error : " + exception.message)
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        if (isTracking) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }
}