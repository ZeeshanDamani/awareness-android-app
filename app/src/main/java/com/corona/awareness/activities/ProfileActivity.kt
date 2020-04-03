package com.corona.awareness.activities

import android.content.Intent
import android.os.Bundle
import com.corona.awareness.Awareness
import com.corona.awareness.R
import com.corona.awareness.adapters.ServayAdapter
import com.corona.awareness.databinding.ActivityProfileBinding
import com.corona.awareness.model.servay.servayResponse

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

        //  val name: List<String> = Awareness.loginData?.user?.username.toString().split(" ")

        bindingView.tFirstName.setText("" + Awareness.loginData?.user?.firstName)
        bindingView.tPhone.setText("" + Awareness.loginData?.user?.phoneNumber)
        bindingView.tEmail.setText("" + Awareness.loginData?.user?.email)
        bindingView.tCnic.setText("" + Awareness.loginData?.user?.cnic)

        bindingView.btEditProfile.setOnClickListener {
            startActivity(Intent(this, ProfileEditActivity::class.java))
        }
        bindingView.btDiagonose.setOnClickListener {
            startActivity(Intent(this, DiagnosisHistoryActivity::class.java))
        }
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


}
