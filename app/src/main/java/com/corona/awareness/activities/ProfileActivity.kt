package com.corona.awareness.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.corona.awareness.R
import com.corona.awareness.configs.AppSharedPreferences
import com.corona.awareness.databinding.ActivityProfileBinding
import com.corona.awareness.helper.kotlin.Constants
import com.corona.awareness.network.model.LoginResponseModel

class ProfileActivity : BaseActivity() {
    private lateinit var bindingView: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView = setContentViewDataBinding(R.layout.activity_profile)
        setUpToolBar()
    }

    override fun onResume() {
        super.onResume()
        setupUI()
    }

    private fun setupUI() {
        val loginResponseModel: LoginResponseModel? =
            AppSharedPreferences.get(Constants.LOGIN_OBJECT)
        loginResponseModel?.let {
            bindingView.tFirstName.text = it.user.firstName
            bindingView.tPhone.text = it.user.phoneNumber

            val email = it.user.email
            if (email.isNullOrBlank()) {
                bindingView.tEmail.visibility = View.GONE
            } else {
                bindingView.tEmail.text = email
            }

            val cnic = it.user.cnic
            if (cnic.isNullOrBlank()) {
                bindingView.tCnic.visibility = View.GONE
            } else {
                bindingView.tCnic.text = cnic
            }
        }

        bindingView.btEditProfile.setOnClickListener {
            startActivity(Intent(this, ProfileEditActivity::class.java))
        }
        bindingView.btDiagonose.setOnClickListener {
            startActivity(Intent(this, DiagnosisHistoryActivity::class.java))
        }

        bindingView.updatePasswordBtn.setOnClickListener {
            startActivity(Intent(this, UpdatePasswordActivity::class.java))
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
}
