package com.corona.awareness

import android.app.DatePickerDialog
import android.os.Bundle
import com.corona.awareness.SignUpActivity.ValidationResult.*
import com.corona.awareness.databinding.ActivitySignupBinding
import java.util.*


class SignUpActivity : BaseActivity() {

    private lateinit var bindingView: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView = setContentViewDataBinding(R.layout.activity_signup)
        setupUI()
    }

    private fun setupUI() {
        bindingView.dateOfBirth.apply {

            fun setDate(year: Int, month: Int, day: Int) {
                setText("$day/$month/$year")
            }

            setOnClickListener {

                val calendar = Calendar.getInstance()
                val currentYear = calendar.get(Calendar.YEAR) // current year
                val currentMonth = calendar.get(Calendar.MONTH) // current month
                val currentDay = calendar.get(Calendar.DAY_OF_MONTH) // current day

                DatePickerDialog(
                    this@SignUpActivity,
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        setDate(year, month, dayOfMonth)
                    }, currentYear, currentMonth, currentDay
                ).show()
            }
        }

        bindingView.signUpBtn.setOnClickListener {
            validateAndSignUp()
        }
    }

    private fun validateAndSignUp() {
        val data = getSignUpData()
        val validationResult = validateData(data)
        if (validationResult == VALID) {
            finish()
        } else {
            showError(validationResult)
        }
    }

    private fun showError(result: ValidationResult) {
        fun resetError() {
            bindingView.firstName.error = null
            bindingView.lastName.error = null
            bindingView.phoneNumber.error = null
            bindingView.password.error = null
            bindingView.passwordConfirmation.error = null
            bindingView.dateOfBirth.error = null
        }

        resetError()

        when (result) {
            INVALID_FIRST_NAME -> bindingView.firstName.error = "Enter first name"
            INVALID_LAST_NAME -> bindingView.lastName.error = "Enter last name"
            INVALID_PHONE -> bindingView.phoneNumber.error = "Enter phone number"
            INVALID_PASSWORD -> bindingView.password.error = "Enter a password"
            PASSWORD_MISS_MATCH -> bindingView.passwordConfirmation.error = "Password miss match"
            INVALID_DOB -> bindingView.dateOfBirth.error = "Select a date"
            else -> {}
        }
    }

    private fun getSignUpData(): SignUpData {
        val firstName = bindingView.firstName.text.toString()
        val lastName = bindingView.lastName.text.toString()
        val phoneNumber = bindingView.phoneNumber.text.toString()
        val cnic = bindingView.cnic.text.toString()
        val password = bindingView.password.text.toString()
        val passwordConfirmation = bindingView.passwordConfirmation.text.toString()
        val email = bindingView.email.text.toString()
        val dateOfBirth = bindingView.dateOfBirth.text.toString()

        return SignUpData(
            firstName,
            lastName,
            phoneNumber,
            cnic,
            password,
            passwordConfirmation,
            email,
            dateOfBirth
        )
    }

    private fun validateData(data: SignUpData): ValidationResult {
        return when {
            data.firstName.isBlank() -> INVALID_FIRST_NAME
            data.lastName.isBlank() -> INVALID_LAST_NAME
            data.phoneNumber.isBlank() -> INVALID_PHONE
            data.password.isBlank() -> INVALID_PASSWORD
            !data.password.contentEquals(data.passwordConfirmation) -> PASSWORD_MISS_MATCH
            data.dateOfBirth.isBlank() -> INVALID_DOB
            else -> VALID
        }
    }


    private enum class ValidationResult {
        VALID,
        INVALID_FIRST_NAME,
        INVALID_LAST_NAME,
        INVALID_PHONE,
        INVALID_PASSWORD,
        PASSWORD_MISS_MATCH,
        INVALID_DOB
    }
}
