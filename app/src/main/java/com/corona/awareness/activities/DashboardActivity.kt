package com.corona.awareness.activities


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.corona.awareness.Awareness
import com.corona.awareness.R
import com.corona.awareness.configs.AppSharedPreferences
import com.corona.awareness.databinding.ActivityDashboardBinding
import com.corona.awareness.helper.kotlin.Constants
import com.corona.awareness.network.RetrofitConnection
import com.corona.awareness.network.model.LoginResponseModel
import com.corona.awareness.network.model.PingRequestModel
import com.corona.awareness.network.model.PingResponseModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DashboardActivity : BaseActivity(), LocationListener,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    public var latitude: String? = null
    public var longitude: String? = null
    private var login: LoginResponseModel? = null
    private lateinit var loginData: LoginResponseModel
    private lateinit var mLocationManager: LocationManager
    val PERMISSION_ID = 42
    private lateinit var bindingView: ActivityDashboardBinding
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView = setContentViewDataBinding(R.layout.activity_dashboard)
        setUpToolBar()
        login = AppSharedPreferences.get<LoginResponseModel>(Constants.LOGIN_OBJECT)
        if (login?.success!!) {
            Awareness.loginData = login
            Log.e("qq user -", "" + login?.token)
            Log.e("qq user?? -", "" + Awareness.loginData!!.token)
        }


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setupUI()
        enableGPSAutomatically()
        // getLastLocation()


        mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 50000, 0F, this)

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
        return if (item.itemId == R.id.profile) {
            goToProfileActivity()
            true
        } else super.onOptionsItemSelected(item)
    }

    fun setupUI() {

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


    override fun onLocationChanged(location: Location?) {
        Log.e("lati ", " " + location?.latitude)
        Log.e("longi ", " " + location?.longitude)
        Constants.latitude = "" + location?.latitude
        Constants.longitude = "" + location?.longitude
        pingUserLocation(location?.latitude.toString(), location?.longitude.toString())
    }


    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

    }

    override fun onProviderEnabled(p0: String?) {

    }

    override fun onProviderDisabled(p0: String?) {

    }

    private fun pingUserLocation(latitude: String, longitude: String) {
        val call = RetrofitConnection.getAPIClient(login?.token!!)
            .sendUserPings("" + login?.user?.id, setRecordRequest(latitude, longitude))
        call.enqueue(object : Callback<PingResponseModel> {
            override fun onFailure(call: Call<PingResponseModel>, t: Throwable) {
                Log.e("DSG ", "" + t.message)
            }

            override fun onResponse(
                call: Call<PingResponseModel>,
                response: Response<PingResponseModel>
            ) {
                if (response.code() == 200) {
                    if (response.isSuccessful) {
                        if (response.body()?.success!!) {
                            Log.e("success", "" + response.body()?.success)
                        }
                    } else {
                        Log.e("Failed", "" + response.errorBody())
                    }

                } else if (response.code() == 401) {
                    goToLginActivity()
                }
            }
        })

    }

    private fun setRecordRequest(latitude: String, longitude: String): PingRequestModel {
        return PingRequestModel(
            "1584960348",
            "ANDROID",
            latitude,
            longitude,
            login?.user?.id!!
        )
    }


    //old work
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        Log.e("DashboardActivity", "Latitude : " + location.latitude.toString())
                        Log.e("DashboardActivity", "Longitude : " + location.longitude.toString())
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
            Log.e("DashboardActivity", "Latitude : " + mLastLocation.latitude.toString())
            Log.e("DashboardActivity", "Longitude : " + mLastLocation.longitude.toString())
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Granted. Start getting the location information
            }
        }
    }
    //old work end here

    //Enable GPS
    private fun enableGPSAutomatically() {
        var googleApiClient: GoogleApiClient? = null
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build()
            googleApiClient!!.connect()
            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = (30 * 1000).toLong()
            locationRequest.fastestInterval = (5 * 1000).toLong()
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
            // **************************
            builder.setAlwaysShow(true) // this is the key ingredient
            // **************************
            val result = LocationServices.SettingsApi
                .checkLocationSettings(googleApiClient, builder.build())
            result.setResultCallback(object : ResultCallback<LocationSettingsResult> {
                override fun onResult(result: LocationSettingsResult) {
                    val status = result.status
                    val state = result
                        .locationSettingsStates
                    when (status.statusCode) {
                        LocationSettingsStatusCodes.SUCCESS -> {
                        }
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                            // Toast.makeText(SplashScreen.this, "Gps is off", Toast.LENGTH_SHORT).show();
                            // Location settings are not satisfied. But could be
                            // fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling
                                // startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(this@DashboardActivity, 1000)
                            } catch (e: IntentSender.SendIntentException) {
                                // Ignore the error.
                            }

                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        }
                    }
                    // Toast.makeText(SplashScreen.this, "Success", Toast.LENGTH_SHORT).show();
                    // All location settings are satisfied. The client can
                    // initialize location
                    // requests here.
                    //   Toast.makeText(SplashScreen.this, "Setting not allowed", Toast.LENGTH_SHORT).show();
                    // Location settings are not satisfied. However, we have
                    // no way to fix the
                    // settings so we won't show the dialog.
                }
            })
        }
    }


    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

}











