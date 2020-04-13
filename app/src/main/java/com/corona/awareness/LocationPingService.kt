package com.corona.awareness

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.*
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.corona.awareness.activities.DashboardActivity
import com.corona.awareness.activities.DashboardActivity.Companion.ERROR_TYPE_LOCATION_OFF
import com.corona.awareness.activities.DashboardActivity.Companion.ERROR_TYPE_PERMISSION_MISSING
import com.corona.awareness.activities.SplashActivity
import com.corona.awareness.configs.AppSharedPreferences
import com.corona.awareness.helper.Constants
import com.corona.awareness.network.RetrofitConnection
import com.corona.awareness.network.model.LoginResponseModel
import com.corona.awareness.network.model.PingRequestModel
import com.corona.awareness.network.model.PingResponseModel
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Tasks
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LocationPingService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val handlerThread = HandlerThread("LocationPingServiceHandlerThread")
    private lateinit var handler: Handler

    private val pingRunnable = object : Runnable {
        override fun run() {
            postLocationUpdate()
            handler.postDelayed(this, PING_FREQUENCY)
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation: Location = locationResult.lastLocation
            postLocation(lastLocation)
        }
    }

    private fun postLocation(location: Location) {
        val loginResponseModel = AppSharedPreferences.get<LoginResponseModel>(Constants.LOGIN_OBJECT)
        val token = loginResponseModel?.token
        if (token != null) {
            val userId = loginResponseModel.user.id
            val pingRequestModel = PingRequestModel(
                System.currentTimeMillis().toString(),
                "",
                location.latitude.toString(),
                location.longitude.toString(),
                userId
            )
            val call = RetrofitConnection.getAPIClient(token)
                .sendUserPings(userId.toString(), pingRequestModel)

            call.enqueue(object : Callback<PingResponseModel> {
                override fun onFailure(call: Call<PingResponseModel>, t: Throwable) {
                    Log.e("LocationPingService", "Failed to update ping")
                }

                override fun onResponse(
                    call: Call<PingResponseModel>,
                    response: Response<PingResponseModel>
                ) {
                    Log.i("LocationPingService", "Ping Update Successfully")
                }
            })

        } else {
            Log.e("LocationPingService", "Access token is null")
            stopSelf()
        }
    }

    private fun isLocationPermissionsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun postLocationUpdate() {
        if (isLocationPermissionsGranted()) {
            requestCurrentLocationAndPost()
        } else {
            Log.e("LocationPingService", "Permissions are missing")
            reportError(ERROR_TYPE_PERMISSION_MISSING)
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
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

    private fun requestCurrentLocationAndPost() {
        val task = fusedLocationClient.lastLocation
            .addOnSuccessListener {
                if (isLocationEnabled()) {
                    if (it == null) {
                        requestNewLocation()
                    } else {
                        postLocation(it)
                    }
                } else {
                    Log.e("LocationPingService", "Location is turned off")
                    reportError(ERROR_TYPE_LOCATION_OFF)
                }
            }
        Tasks.await(task)
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        handlerThread.start()
        handler = Handler(handlerThread.looper)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val activityIntent = Intent(this, SplashActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, activityIntent, 0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name) + " is running")
            .setSmallIcon(R.drawable.ic_logo)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
        handler.post(pingRunnable)
        return START_STICKY
    }

    override fun onDestroy() {
        handler.removeCallbacks(pingRunnable)
        handlerThread.quit()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val chan = NotificationChannel(CHANNEL_ID, "Awareness App Channel", NotificationManager.IMPORTANCE_NONE)
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
    }

    private fun reportError(errorType: Int) {
        val broadCastIntent = Intent()
        broadCastIntent.action = DashboardActivity.LOCATION_PING_ERROR
        broadCastIntent.putExtra(DashboardActivity.ERROR_TYPE, errorType)
        sendBroadcast(broadCastIntent)
    }

    companion object {
        private const val CHANNEL_ID = "awarenessApp"
        private const val ONE_SECOND_IN_MILLIS = 1000L
        private const val PING_FREQUENCY = 60 * ONE_SECOND_IN_MILLIS
    }
}