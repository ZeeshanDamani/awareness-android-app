package com.corona.awareness.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.corona.awareness.R
import com.corona.awareness.configs.AppSharedPreferences
import com.corona.awareness.helper.kotlin.Constants
import com.corona.awareness.network.RetrofitConnection
import com.corona.awareness.network.model.City
import com.corona.awareness.network.model.LoginResponseModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {

    var playServiceDialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val result = googleApiAvailability.isGooglePlayServicesAvailable(this)
        if (result == ConnectionResult.SUCCESS) {
            val loginResponseModel: LoginResponseModel? =
                AppSharedPreferences.get(Constants.LOGIN_OBJECT)
            val user = loginResponseModel?.user
            if (loginResponseModel?.token.isNullOrBlank() || user == null) {
                fetchCityInfo()
            } else
                goToDashBoard()
        } else {
            playServiceDialog =
                googleApiAvailability.getErrorDialog(this, result, REQUEST_PLAY_SERVICE)
            playServiceDialog?.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        playServiceDialog?.let {
            if (it.isShowing) it.dismiss()
        }
    }

    private fun fetchCityInfo() {
        val call = RetrofitConnection.getAPIClient().getCities()
        call.enqueue(object : Callback<List<City>> {
            override fun onFailure(call: Call<List<City>>, t: Throwable) {
                fetchCityInfo()
            }

            override fun onResponse(call: Call<List<City>>, response: Response<List<City>>) {
                AppSharedPreferences.put(response.body(), Constants.CITIES)
                goToLoginActivity()
            }
        })
    }

    private fun goToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToDashBoard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val REQUEST_PLAY_SERVICE = 111
    }
}
