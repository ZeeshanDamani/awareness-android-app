package com.corona.awareness.activities


import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.corona.awareness.Awareness
import com.corona.awareness.LocationPingService
import com.corona.awareness.R
import com.corona.awareness.configs.AppSharedPreferences
import com.corona.awareness.databinding.ActivityDashboardBinding
import com.corona.awareness.helper.Constants
import com.corona.awareness.network.model.LoginResponseModel
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Tasks
import com.google.android.material.snackbar.Snackbar


class DashboardActivity : BaseActivity() {

    private lateinit var bindingView: ActivityDashboardBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationPingErrorBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (it.action == LOCATION_PING_ERROR) {
                    val errorType = it.getIntExtra(ERROR_TYPE, -1)
                    if (errorType == ERROR_TYPE_PERMISSION_MISSING) {
                        requestLocationPermissions(LOCATIONS_SERVICE_PERMISSION_REQUEST_CODE)
                    } else if (errorType == ERROR_TYPE_LOCATION_OFF) {
                        Snackbar.make(
                            bindingView.container,
                            "Please turn on your location",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView = setContentViewDataBinding(R.layout.activity_dashboard)
        setUpToolBar()
        setupUI()
        Awareness.getLoginData()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter()
        intentFilter.addAction(LOCATION_PING_ERROR)
        registerReceiver(locationPingErrorBroadcastReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(locationPingErrorBroadcastReceiver)
    }

    private fun setUpToolBar() {
        setSupportActionBar(bindingView.toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.title = ""
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.dashboard_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.profile -> {
                goToProfileActivity()
                true
            }
            R.id.logout -> {
                AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to delete this entry?")
                    .setPositiveButton(android.R.string.yes) { _, _ ->
                        AppSharedPreferences.remove(Constants.LOGIN_OBJECT)
                        val intent = Intent(this, LocationPingService::class.java)
                        stopService(intent)
                        goToLoginActivity()
                    }
                    .setNegativeButton(android.R.string.no) { dialog, _ -> dialog.dismiss() }
                    .show()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupUI() {

        bindingView.aboutCoronaButton.setOnClickListener {
            goToAboutActivity()
        }

        bindingView.whoGuidelines.setOnClickListener {
            goToGuidelineActivity()
        }

        bindingView.notFeelWell.setOnClickListener {
            val loginResponseModel: LoginResponseModel? =
                AppSharedPreferences.get(Constants.LOGIN_OBJECT)
            val isProfileComplete = loginResponseModel?.user?.profileCompleted ?: false
            if (isProfileComplete) {
                goToFeelingSickActivity()
            } else {
                Snackbar.make(
                    bindingView.container,
                    "Please update your profile",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        bindingView.nearestCenters.setOnClickListener {
            goToNearestCenterActivity()
        }
    }

    private fun goToProfileActivity() {
        startActivity(Intent(this, ProfileActivity::class.java))
    }

    private fun goToFeelingSickActivity() {
        if (isLocationPermissionsGranted()) {
            requestCurrentLocationAndStart()
        } else {
            requestLocationPermissions()
        }
    }

    private fun requestCurrentLocationAndStart() {
        val task = fusedLocationClient.lastLocation
            .addOnSuccessListener {
                if (isLocationEnabled()) {
                    if (it == null) {
                        requestNewLocation()
                    } else {
                        startFeelingSickActivity(it)
                    }
                } else {
                    Snackbar.make(
                        bindingView.container,
                        "Please turn on your location",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

        Thread(Runnable {
            Tasks.await(task)
        }).start()
    }

    private fun startFeelingSickActivity(location: Location) {
        val intent = Intent(this, FeelingSickActivity::class.java)
        intent.putExtra(FeelingSickActivity.LATITUDE, location.latitude)
        intent.putExtra(FeelingSickActivity.LONGITUDE, location.longitude)
        startActivity(intent)
    }

    private fun requestNewLocation() {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationClient.requestLocationUpdates(
            locationRequest, locationCallback,
            Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation: Location = locationResult.lastLocation
            startFeelingSickActivity(lastLocation)
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun isLocationPermissionsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            applicationContext,
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            applicationContext,
            ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermissions(requestCode: Int = LOCATIONS_PERMISSION_REQUEST_CODE) {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION),
            requestCode
        )
    }

    private fun goToAboutActivity() {
        val intent = Intent(this, AboutCoronaActivity::class.java)
        startActivity(intent)
    }

    private fun goToGuidelineActivity() {
        val intent = Intent(this, WhoGuidelinesActivity::class.java)
        startActivity(intent)
    }

    private fun goToNearestCenterActivity() {
        val intent = Intent(this, NearestCenterActivity::class.java)
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATIONS_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestCurrentLocationAndStart()
            } else {
                Snackbar.make(
                    bindingView.container,
                    "Locations permission required",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        private const val LOCATIONS_PERMISSION_REQUEST_CODE = 12
        private const val LOCATIONS_SERVICE_PERMISSION_REQUEST_CODE = 13
        const val LOCATION_PING_ERROR = "LOCATION_PING_ERROR"
        const val ERROR_TYPE = "ERROR_TYPE"
        const val ERROR_TYPE_PERMISSION_MISSING = 1
        const val ERROR_TYPE_LOCATION_OFF = 2
    }
}











