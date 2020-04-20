package com.corona.awareness.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import com.corona.awareness.Awareness
import com.corona.awareness.R
import com.corona.awareness.configs.AppSharedPreferences
import com.corona.awareness.databinding.ActivityLoginBinding
import com.corona.awareness.helper.Constants
import com.corona.awareness.helper.PhoneNumberValidator.isValidPhoneNumber
import com.corona.awareness.network.RetrofitConnection
import com.corona.awareness.network.model.LoginRequestModel
import com.corona.awareness.network.model.LoginResponseModel
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {

    private var progressDialog: ProgressDialog? = null
    private lateinit var bindingView: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView = setContentViewDataBinding(R.layout.activity_login)
        AppSharedPreferences.init(this)
        setupUI()
    }

    override fun onStop() {
        super.onStop()
        resetProgressDialog()
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
        startActivityForResult(intent, SIGN_UP_REQUEST_CODE)
    }

    private fun performAuth() {
        val loginData = getLoginData()
        val validateResult = validateData(loginData)
        if (validateResult == ValidationResult.VALID) {
            progressDialog = ProgressDialog.show(this, "", "Please wait", true)
            loginUser(loginData)
        } else {
            showError(validateResult)
        }
    }

    private fun getLoginData(): LoginRequestModel {
        val userPhoneNumber = bindingView.userPhone.text.toString()
        val userPassword = bindingView.password.text.toString()

        return LoginRequestModel(
            userPhoneNumber,
            userPassword
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_UP_REQUEST_CODE && resultCode == SIGN_UP_RESULT_CODE) {
            Snackbar.make(
                bindingView.container,
                "Account created successfully",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun resetProgressDialog() {
        progressDialog?.dismiss()
        progressDialog = null
    }

    private fun loginUser(loginRequest: LoginRequestModel) {

        fun onLoginFailure() {
            Snackbar.make(
                bindingView.container,
                "Failed to login, please again",
                Snackbar.LENGTH_SHORT
            ).show()
        }

        val call = RetrofitConnection.getAPIClient("").loginUser(loginRequest)
        call.enqueue(object : Callback<LoginResponseModel> {
            override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                onLoginFailure()
                resetProgressDialog()
            }

            override fun onResponse(
                call: Call<LoginResponseModel>,
                response: Response<LoginResponseModel>
            ) {
                resetProgressDialog()
                val loginResponse = response.body()
                AppSharedPreferences.put(loginResponse, Constants.LOGIN_OBJECT)
                Awareness.loginData = loginResponse
                goToDashboardActivity()
            }
        })

    }


    private fun validateData(data: LoginRequestModel): ValidationResult {
        return when {
            !isValidPhoneNumber(data.userPhoneNumber) -> ValidationResult.INVALID_PHONE
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
            ValidationResult.INVALID_PHONE -> bindingView.userPhone.error = "Enter a valid phone number"
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

    companion object {
        const val SIGN_UP_REQUEST_CODE = 99
        const val SIGN_UP_RESULT_CODE = 199
    }
}
