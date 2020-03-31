package com.corona.awareness.activities

import android.os.Bundle
import android.util.Log
import com.corona.awareness.Awareness
import com.corona.awareness.R
import com.corona.awareness.adapters.ServayAdapter
import com.corona.awareness.databinding.ActivityProfileBinding
import com.corona.awareness.model.servay.servayResponse
import com.corona.awareness.network.RetrofitConnection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : BaseActivity() ,ServayAdapter.ViewHolder.servayListener{

    private lateinit var servayViewAdapter: ServayAdapter
    private lateinit var servayListener: ServayAdapter.ViewHolder.servayListener
    private lateinit var bindingView: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_profile)
        bindingView = setContentViewDataBinding(R.layout.activity_profile)
        setTitle("Profile")
        servayListener = this
        setUpToolBar()
        setupUI()
    }

    private fun setupUI() {

        val name: List<String> = Awareness.loginData?.user?.username.toString().split(" ")

        bindingView.tFirstName.setText("" + Awareness.loginData?.user?.firstName)
        bindingView.tPhone.setText("" + Awareness.loginData?.user?.phoneNumber)
        bindingView.tEmail.setText(""+Awareness.loginData?.user?.email)
        bindingView.tCnic.setText("" + Awareness.loginData?.user?.cnic)

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

    override fun onClick(servayResponse: servayResponse.UserSurvey) {

    }

    private fun setupServays() {

        val call =
            RetrofitConnection.getAPIClient(Awareness?.loginData?.token!!)
                .getAllUserServeys(""+Awareness.loginData?.user?.id)

        call.enqueue(object : Callback<servayResponse> {
            override fun onFailure(call: Call<servayResponse>, t: Throwable) {
                Log.e("qq P-error", "" + t.message)
            }

            override fun onResponse(
                call: Call<servayResponse>,
                response: Response<servayResponse>
            ) {
                if (response.code() == 200) {
                    if (response.isSuccessful) {
                        Log.e("qq Data", "" + response.body()?.userSurveys?.get(0)?.assessmentTime)
                       // bindingView.previousServayRecyclerview.adapter = ServayAdapter(response.body()!!.userSurveys,servayListener)


                    } else {
                        Log.e("qq error", "" + response.errorBody())
                    }

                } else {
                    goToLginActivity()
                }
            }

        })

    }
}
