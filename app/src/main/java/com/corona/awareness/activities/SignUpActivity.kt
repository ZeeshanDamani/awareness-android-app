package com.corona.awareness.activities

import android.app.ProgressDialog
import android.os.Bundle
import com.corona.awareness.R
import com.corona.awareness.activities.LoginActivity.Companion.SIGN_UP_RESULT_CODE
import com.corona.awareness.activities.SignUpActivity.ValidationResult.*
import com.corona.awareness.databinding.ActivitySignupBinding
import com.corona.awareness.helper.PhoneNumberValidator.isValidPhoneNumber
import com.corona.awareness.network.RetrofitConnection
import com.corona.awareness.network.model.SignUpRequestModel
import com.corona.awareness.network.model.SignUpResponseModel
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignUpActivity : BaseActivity() {

    private lateinit var bindingView: ActivitySignupBinding
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView = setContentViewDataBinding(R.layout.activity_signup)
        setUpToolBar()
        setupUI()
    }

    override fun onStop() {
        super.onStop()
        resetProgressDialog()
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

    private fun setupUI() {
        bindingView.signUpBtn.setOnClickListener {
            validateAndSignUp()
        }
    }

    private fun validateAndSignUp() {
        val data = getSignUpData()
        val validationResult = validateData(data)
        if (validationResult == VALID) {
            signUpUser(data)
        } else {
            showError(validationResult)
        }
    }

    private fun showError(result: ValidationResult) {
        fun resetError() {
            bindingView.fullName.error = null
            bindingView.phoneNumber.error = null
            bindingView.password.error = null
            bindingView.passwordConfirmation.error = null
        }

        resetError()

        when (result) {
            INVALID_NAME -> bindingView.fullName.error = "Enter full name"
            INVALID_PHONE -> bindingView.phoneNumber.error = "Enter a valid phone number"
            INVALID_PASSWORD -> bindingView.password.error = "Enter a password"
            PASSWORD_MISS_MATCH -> bindingView.passwordConfirmation.error = "Password miss match"
            else -> {}
        }
    }

    private fun getSignUpData(): SignUpData {
        val phoneNumber = bindingView.phoneNumber.text.toString()
        val fullName = bindingView.fullName.text.toString()
        val password = bindingView.password.text.toString()
        val passwordConfirmation = bindingView.passwordConfirmation.text.toString()
        return SignUpData(fullName, phoneNumber, password, passwordConfirmation)
    }

    private fun validateData(data: SignUpData): ValidationResult {
        return when {
            data.fullName.isBlank() -> INVALID_NAME
            !isValidPhoneNumber(data.phoneNumber) -> INVALID_PHONE
            data.password.isBlank() -> INVALID_PASSWORD
            !matchesPassword(data.password, data.passwordConfirmation) -> PASSWORD_MISS_MATCH
            else -> VALID
        }
    }

    private fun matchesPassword(password: String, passwordConfirmation: String): Boolean {
        return password == passwordConfirmation
    }

    private data class SignUpData(
        val fullName: String,
        val phoneNumber: String,
        val password: String,
        val passwordConfirmation: String
    )

    private fun SignUpData.toSignUpRequestModel(): SignUpRequestModel {
        return SignUpRequestModel(
            phoneNumber,
            fullName,
            password
        )
    }

    private enum class ValidationResult {
        VALID,
        INVALID_NAME,
        INVALID_PHONE,
        INVALID_PASSWORD,
        PASSWORD_MISS_MATCH,
    }

    private fun resetProgressDialog() {
        progressDialog?.dismiss()
        progressDialog = null
    }

    private fun signUpUser(data: SignUpData) {

        fun onSignUpFailure() {
            Snackbar.make(
                bindingView.container,
                "Failed to create the account, please again",
                Snackbar.LENGTH_SHORT
            ).show()
        }

        val requestModel = data.toSignUpRequestModel()
        progressDialog = ProgressDialog.show(this, "", "Creating account please wait", true)
        val call = RetrofitConnection.getAPIClient("").signUpUser(requestModel)

        call.enqueue(object : Callback<SignUpResponseModel> {
            override fun onFailure(call: Call<SignUpResponseModel>, t: Throwable) {
                resetProgressDialog()
                onSignUpFailure()
            }

            override fun onResponse(
                call: Call<SignUpResponseModel>,
                response: Response<SignUpResponseModel>
            ) {
                resetProgressDialog()
                if (response.isSuccessful) {
                    setResult(SIGN_UP_RESULT_CODE)
                    finish()
                } else {
                    onSignUpFailure()
                }
            }
        })

    }


}
