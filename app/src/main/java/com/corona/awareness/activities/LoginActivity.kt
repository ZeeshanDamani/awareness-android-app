package com.corona.awareness.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.corona.awareness.R
import com.corona.awareness.configs.AppSharedPreferences
import com.corona.awareness.databinding.ActivityLoginBinding
import com.corona.awareness.helper.kotlin.Constants
import com.corona.awareness.helper.kotlin.CustomProgressBar
import com.corona.awareness.helper.kotlin.Utils
import com.corona.awareness.model.login.loginRequest
import com.corona.awareness.model.login.loginResponse
import com.corona.awareness.network.RetrofitConnection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {

    private var progressBar: CustomProgressBar? = null
    private lateinit var bindingView: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView = setContentViewDataBinding(R.layout.activity_login)
        AppSharedPreferences.init(this)
        setupUI()
    }

    private fun setupUI() {
        bindingView.labelCreateAnAccount.setOnClickListener {
            goToSignUpActivity()
        }
        bindingView.loginBtn.setOnClickListener {
            performAuth()
        }
    }

    private fun goToSignUpActivity() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun performAuth() {
        val userPhone = bindingView.userPhone.text.toString();
        val userPassword = bindingView.password.text.toString();

        //  if (isAuthenticate(userPhone, userPassword)) {
        val loginData = getloginData()
        val validateResult = validateData(loginData)
        if (validateResult == ValidationResult.VALID) {
            progressBar = Utils.openProgressDialog(this)
            loginUser(loginData)
        } else {
            showError(validateResult)
        }

//        } else {
////            bindingView.invalidAuthText.text = "invalid login"
////            bindingView.invalidAuthText.visibility = View.VISIBLE
//        }
    }

    private fun isAuthenticate(phoneNumber: String, password: String): Boolean {
        val user = AppSharedPreferences.getUser()
        return phoneNumber == user.phoneNumber
                && password == user.password
    }


    private fun getloginData(): loginRequest {
        val userPhoneNumber = bindingView.userPhone.text.toString()
        val userPassword = bindingView.password.text.toString()

        return loginRequest(
            userPhoneNumber,
            userPassword
        )
    }

    private fun loginUser(loginRequest: loginRequest) {

        val call = RetrofitConnection.getAPIClient("").loginUser(loginRequest)
        call.enqueue(object : Callback<loginResponse> {
            override fun onFailure(call: Call<loginResponse>, t: Throwable) {
                progressBar?.dialog?.dismiss()
            }

            override fun onResponse(call: Call<loginResponse>, response: Response<loginResponse>) {
                if (response.code() == 200) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            progressBar?.dialog?.dismiss()
                            val loginResponse = response.body()
                            if (loginResponse?.success!!) {
                                AppSharedPreferences.put(loginResponse, Constants.LOGIN_OBJECT)
                                goToDashboardActivity()
                                //  Log.e("login", "" + loginResponse?.city?.name)
                                Log.e("login", "" + loginResponse?.token)
                            } else {
                                Log.e("login-Failed", loginResponse.message)
                            }

                        }
                    }
                }
            }

        })

    }


    private fun validateData(data: loginRequest): ValidationResult {
        return when {
            data.userPhoneNumber.isBlank() -> ValidationResult.INVALID_PHONE
            data.userPassword.isBlank() -> ValidationResult.INVALID_PASSWORD
            else -> ValidationResult.VALID
        }
    }

    private fun showError(result: ValidationResult) {
        fun resetError() {
            bindingView.userPhone.error = null
            bindingView.password.error = null

        }

        resetError()

        when (result) {
            ValidationResult.INVALID_PHONE -> bindingView.userPhone.error = "Enter phone number"
            ValidationResult.INVALID_PASSWORD -> bindingView.password.error = "Enter a password"

            else -> {
            }
        }
    }

    private enum class ValidationResult {
        VALID,
        INVALID_PHONE,
        INVALID_PASSWORD
    }


}
