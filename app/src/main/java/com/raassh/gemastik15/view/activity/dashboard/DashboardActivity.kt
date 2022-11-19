package com.raassh.gemastik15.view.activity.dashboard

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.raassh.gemastik15.R
import com.raassh.gemastik15.databinding.ActivityDashboardBinding
import com.raassh.gemastik15.services.ChatService
import com.raassh.gemastik15.utils.checkPermission
import com.raassh.gemastik15.utils.showSnackbar
import com.raassh.gemastik15.view.activity.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private val viewModel by viewModel<DashboardViewModel>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val waitSettingIntentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        getMyLastLocation()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                getMyLastLocation()
            }
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                getMyLastLocation()
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
                        waitSettingIntentLauncher.launch(intent)
                    }
                    .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
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
                    Log.d("Location", "Location: ${location.latitude}, ${location.longitude}")
                    viewModel.setLocation(LatLng(location.latitude, location.longitude))
                } else {
                    Log.d("Location", "Location: null")
                    viewModel.setLocation(null)
                    binding.root.showSnackbar(getString(R.string.location_not_found))
                }
            }
        } else {
            Log.d("DashboardActivity", "Permission not granted")
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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getMyLastLocation()

        val navView = binding.bottomNavView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        val navController = navHostFragment.navController

        WindowCompat.setDecorFitsSystemWindows(window, false)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.searchByFacilityFragment,
                R.id.searchFacilityOptionFragment,
                R.id.searchResultFragment,
                R.id.placeDetailFragment,
                R.id.addContributionFragment,
                R.id.webViewerFragment,
                R.id.reviewsFragment,
                R.id.reviewHistoryFragment,
                R.id.reportReviewFragment,
                R.id.editContributionFragment,
                R.id.changePasswordFragment,
                R.id.editProfileFragment,
                R.id.moderationFragment,
                R.id.detailContributionReportFragment,
                R.id.detailUserReportFragment,
                R.id.changeDisabilityFragment,
                R.id.searchUserFragment,
                R.id.chatFragment-> {
                    binding.bottomNavView.visibility = View.GONE
                }
                R.id.accountFragment,
                R.id.navigation_contribute,
                R.id.navigation_read,
                R.id.navigation_discover -> {
                    binding.bottomNavView.visibility = View.VISIBLE
                }
                else -> {
                    binding.bottomNavView.visibility = View.VISIBLE
                }
            }
        }

        navView.setupWithNavController(navController)

        onBackPressedDispatcher.addCallback {
            if (!navController.navigateUp()) {
                finish()
            }
        }

        viewModel.apply {
            getToken().observe(this@DashboardActivity) {
                if (it == null) {
                    val intent = Intent(this@DashboardActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            getUsername().observe(this@DashboardActivity) {
                if (!it.isNullOrBlank()) {
                    val intent = Intent(this@DashboardActivity, ChatService::class.java)
                    intent.putExtra(ChatService.USERNAME, it)
                    startService(intent)
                }
            }

            theme.observe(this@DashboardActivity) {
                if (it != null) {
                    com.raassh.gemastik15.utils.setTheme(it)
                }
            }
        }




        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getMyLastLocation()

        // might contain bugs, need to test more later
        binding.root.accessibilityDelegate = object : View.AccessibilityDelegate() {
            override fun onRequestSendAccessibilityEvent(
                host: ViewGroup,
                child: View,
                event: AccessibilityEvent
            ): Boolean {
                if (event.eventType == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED) {
                    val v = currentFocus

                    if (v is TextInputEditText && child !is TextInputLayout) {
                        v.clearFocus()
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
                    }
                }
                return super.onRequestSendAccessibilityEvent(host, child, event)
            }
        }

    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is TextInputEditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
                }
            }
        }

        return super.dispatchTouchEvent(event)
    }
}