package com.corona.awareness

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.corona.awareness.configs.AppSharedPreferences
import com.corona.awareness.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity() {

    private lateinit var bindingView: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView = setContentViewDataBinding(R.layout.activity_login)
        AppSharedPreferences.init(this)
        setupUI()
    }

    private fun setupUI() {
        bindingView.signUpBtn.setOnClickListener {

            goToSignUpActivity()

        }
        bindingView.loginBtn.setOnClickListener{
            performAuth()
        }
    }

    private fun goToSignUpActivity() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }



    fun performAuth(){
        val userPhone = bindingView.userPhone.text.toString();
        val userPassword = bindingView.password.text.toString();

        if(isAuthenticate(userPhone , userPassword)){
            goToDashboardActivity();
        } else {
            bindingView.invalidAuthText.text = "Invalid Login Credentials"
            bindingView.invalidAuthText.visibility =  View.VISIBLE
        }
    }


    fun isAuthenticate(phoneNumber:String, password:String) : Boolean {
        return (phoneNumber.equals(AppSharedPreferences.getConfig("userPhone"))
                && password.equals(AppSharedPreferences.getConfig("userPassword")))
    }

    private fun goToDashboardActivity() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}
