package com.corona.awareness


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.corona.awareness.databinding.ActivityDashboardBinding
import com.google.android.gms.location.*

class DashboardActivity : BaseActivity() {

    private lateinit var sharedPreferences: SharedPreferences;
    val PERMISSION_ID = 42

    private lateinit var bindingView: ActivityDashboardBinding
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView = setContentViewDataBinding(R.layout.activity_dashboard)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setTitle("Dashboard")
        setupUI()
    }

    fun setupUI() {
        bindingView.aboutCoronaButton.setOnClickListener {

            goToAboutActivity()

        }

        bindingView.whoGuidelines.setOnClickListener {

            goToGuidelineActivity()

        }

        bindingView.notFeelWell.setOnClickListener {
            goToFeelingSickActivity()
            //getLastLocation()
        }

        bindingView.nearestCenters.setOnClickListener {
            goToNearestCenterActivity()
        }
    }

    private fun goToFeelingSickActivity() {
        val intent = Intent(this, FeelingSickActivity::class.java)
        startActivity(intent)
    }

    fun goToAboutActivity() {
        val intent = Intent(this, AboutCoronaActivity::class.java)
        startActivity(intent)
    }

    fun goToGuidelineActivity() {
        val intent = Intent(this, WhoGuidelinesActivity::class.java)
        startActivity(intent)
    }

    fun goToNearestCenterActivity() {
        val intent = Intent(this, NearestCenterActivity::class.java)
        startActivity(intent)
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
//                        findViewById<TextView>(R.id.latTextView).text = location.latitude.toString()
//                        findViewById<TextView>(R.id.lonTextView).text = location.longitude.toString()
                            Log.i("DashboardActivity","Latitude : "  +  location.latitude.toString() )
                           Log.i("DashboardActivity","Longitude : "  +  location.longitude.toString() )
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }


    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
//            findViewById<TextView>(R.id.latTextView).text = mLastLocation.latitude.toString()
//            findViewById<TextView>(R.id.lonTextView).text = mLastLocation.longitude.toString()
            Log.i("DashboardActivity","Latitude : "  +  mLastLocation.latitude.toString() )
            Log.i("DashboardActivity","Longitude : "  +  mLastLocation.longitude.toString() )
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Granted. Start getting the location information
            }
        }
    }
}
