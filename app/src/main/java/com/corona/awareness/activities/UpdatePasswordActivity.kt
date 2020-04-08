package com.corona.awareness.activities

import android.os.Bundle
import com.corona.awareness.Awareness
import com.corona.awareness.R
import com.corona.awareness.activities.UpdatePasswordActivity.ValidationResult.*
import com.corona.awareness.databinding.ActivityUpdatePasswordBinding
import com.corona.awareness.model.PasswordUpdaterRequestModel
import com.corona.awareness.network.RetrofitConnection
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdatePasswordActivity : BaseActivity() {

    private lateinit var bindingView: ActivityUpdatePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView = setContentViewDataBinding(R.layout.activity_update_password)
        setUpToolBar()
        setupUI()
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
        bindingView.updatePasswordBtn.setOnClickListener {
            val data = getPasswordData()
            val result = validatePasswordData(data)
            if (result == VALID) {
                updatePassword(data)
            } else {
                showError(result)
            }
        }
    }

    private fun updatePassword(data: PasswordData) {
        val passwordUpdateRequestModel = PasswordUpdaterRequestModel(
            data.currentPassword,
            data.newPassword
        )

        val call = RetrofitConnection.getAPIClient(Awareness?.loginData?.token!!).updatePassword(
            Awareness.getLoginData()?.user?.id.toString(),
            passwordUpdateRequestModel
        )

        call.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Snackbar.make(
                    bindingView.container,
                    "Failed to update the password",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Snackbar.make(
                    bindingView.container,
                    "Password updated",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun showError(result: ValidationResult) {
        fun resetError() {
            bindingView.oldPassword.error = null
            bindingView.newPassword.error = null
            bindingView.passwordConfirmation.error = null
        }

        resetError()

        when(result) {
            INVALID_PASSWORD -> bindingView.oldPassword.error = "Please enter your current password"
            INVALID_NEW_PASSWORD -> bindingView.newPassword.error = "Please enter a password"
            PASSWORD_MISS_MATCH -> bindingView.passwordConfirmation.error = "Password miss match"
            else -> {}
        }
    }

    private fun validatePasswordData(data: PasswordData): ValidationResult {
        return when {
            data.currentPassword.isBlank() -> INVALID_PASSWORD
            data.newPassword.isBlank() -> INVALID_NEW_PASSWORD
            data.newPassword != data.newPasswordConfirmation -> PASSWORD_MISS_MATCH
            else -> VALID
        }
    }

    private fun getPasswordData(): PasswordData {
        return PasswordData(
            bindingView.oldPassword.text.toString(),
            bindingView.newPassword.text.toString(),
            bindingView.passwordConfirmation.text.toString()
        )
    }

    private data class PasswordData(
        val currentPassword: String,
        val newPassword: String,
        val newPasswordConfirmation: String
    )

    private enum class ValidationResult {
        VALID,
        INVALID_PASSWORD,
        INVALID_NEW_PASSWORD,
        PASSWORD_MISS_MATCH,
    }

}
