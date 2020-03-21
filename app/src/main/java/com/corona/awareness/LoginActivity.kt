package com.corona.awareness

import android.content.Intent
import android.os.Bundle
import com.corona.awareness.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity() {

    private lateinit var bindingView: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView = setContentViewDataBinding(R.layout.activity_login)
        setupUI()
    }

    private fun setupUI() {
        bindingView.signUpBtn.setOnClickListener {
            goToSignUpActivity()
        }
    }

    private fun goToSignUpActivity() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
}
