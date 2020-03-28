package com.corona.awareness.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import com.corona.awareness.R
import com.corona.awareness.activities.SignUpActivity.ValidationResult.*
import com.corona.awareness.configs.AppSharedPreferences
import com.corona.awareness.databinding.ActivitySignupBinding
import com.corona.awareness.model.User
import com.corona.awareness.model.signup.signupRequest
import com.corona.awareness.model.signup.signupResponse
import com.corona.awareness.network.RetrofitConnection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class SignUpActivity : BaseActivity() {

    private lateinit var bindingView: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView = setContentViewDataBinding(R.layout.activity_signup)
        setTitle("Signup")
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
//            signupUser(data.userPhoneNumber,data.firstName,data.lastName,data.userEmail,
//                       data.userPassword,data.dateOfBirth,1,1,data.cnic,"USER")
            signupUser(data)
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
            bindingView.email.error = null
            bindingView.dateOfBirth.error = null
        }

        resetError()

        when (result) {
            INVALID_FIRST_NAME -> bindingView.firstName.error = "Enter first name"
            INVALID_LAST_NAME -> bindingView.lastName.error = "Enter last name"
            INVALID_PHONE -> bindingView.phoneNumber.error = "Enter phone number"
            INVALID_PASSWORD -> bindingView.password.error = "Enter a password"
            INVALID_EMAIL -> bindingView.email.error = "Invalid email"
            PASSWORD_MISS_MATCH -> bindingView.passwordConfirmation.error = "Password miss match"
            INVALID_DOB -> bindingView.dateOfBirth.error = "Select a date"
            else -> {}
        }
    }

    private fun getSignUpData(): signupRequest {
        val phoneNumber = bindingView.phoneNumber.text.toString()
        val firstName = bindingView.firstName.text.toString()
        val lastName = bindingView.lastName.text.toString()
        val cnic = bindingView.cnic.text.toString()
        val password = bindingView.password.text.toString()
        val email = bindingView.email.text.toString()
        val dateOfBirth = bindingView.dateOfBirth.text.toString()

        return signupRequest(
            phoneNumber,
            firstName,
            lastName,
            email,
            password,
            dateOfBirth,
            1,
            1,
            cnic,
            "USER"
        )
    }

    private fun validateData(data: signupRequest): ValidationResult {
        return when {
            data.firstName.isBlank() -> INVALID_FIRST_NAME
            data.lastName.isBlank() -> INVALID_LAST_NAME
            data.userPhoneNumber.isBlank() -> INVALID_PHONE
            data.userPassword.isBlank() -> INVALID_PASSWORD
            !data.userPassword.contentEquals(bindingView.passwordConfirmation.text.toString()) -> PASSWORD_MISS_MATCH
            Patterns.EMAIL_ADDRESS.toRegex().matches(data.userEmail) -> INVALID_EMAIL
            data.dateOfBirth.isBlank() -> INVALID_DOB
            else -> VALID
        }
    }

    private data class SignUpData(
        val firstName: String,
        val lastName: String,
        val phoneNumber: String,
        val cnic: String,
        val password: String,
        val passwordConfirmation: String,
        val email: String,
        val dateOfBirth: String
    )

    private fun SignUpData.toUser(): User {
        return User(
            firstName,
            lastName,
            phoneNumber,
            cnic,
            password,
            email,
            dateOfBirth
        )
    }

    private enum class ValidationResult {
        VALID,
        INVALID_FIRST_NAME,
        INVALID_LAST_NAME,
        INVALID_PHONE,
        INVALID_PASSWORD,
        INVALID_EMAIL,
        PASSWORD_MISS_MATCH,
        INVALID_DOB
    }

    private fun signupUser(signupRequest: signupRequest) {

        var call = RetrofitConnection.getAPIClient("").signupUser(signupRequest)

        call.enqueue(object : Callback<signupResponse>{
            override fun onFailure(call: Call<signupResponse>, t: Throwable) {
                Log.e("signupW Error" ,""+t.message)
            }

            override fun onResponse(
                call: Call<signupResponse>,
                response: Response<signupResponse>
            ) {
                if(response.isSuccessful){
                    val signupResponse = response.body()

                        AppSharedPreferences.saveUser(signupRequest)
                        finish()

                    Log.e("qq" ,""+signupResponse?.success)
                    Log.e("qq" ,""+signupResponse?.responseCode)
                    Log.e("qq" ,""+signupResponse?.message)
                }else{
                    Log.e("qq->" ,""+response.errorBody())
                }
            }

        })


        // var gson = Gson()
        //retrofit call with courotines
//        CoroutineScope(Dispatchers.IO).launch {
//            val response = RetrofitConnection.getAPIClient("").signupUser(phone,firstName,lastName,userEmail,userPassword,
//                                                                        dateOfBirth,cityId,countryId,cnic,accessType)
//            withContext(Dispatchers.Main){
//                try {
//                    if (response.isSuccessful) {
//
//                        Log.e("signupResponse - ", ""+response.code())
//
//                        AppSharedPreferences.saveUser(signupRequest)
//                        finish()
//
//                        //Do something with response e.g show to the UI.
//                    } else {
//                        print("Error: ${response.code()}")
//                    }
//                } catch (e: HttpException) {
//                    print("Exception ${e.message}")
//                    //toast()
//                } catch (e: Throwable) {
//                    print("Ooops: Something else went wrong")
//                }
//            }
//
//        }

    }



}
