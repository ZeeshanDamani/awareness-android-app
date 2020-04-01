package com.corona.awareness.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.corona.awareness.Awareness
import com.corona.awareness.R
import com.corona.awareness.databinding.ActivityProfileEditBinding
import com.corona.awareness.helper.kotlin.CustomProgressBar
import com.corona.awareness.helper.kotlin.Utils
import com.corona.awareness.model.profile.profileRequest
import com.corona.awareness.model.profile.profileResponse
import com.corona.awareness.network.RetrofitConnection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileEditActivity : BaseActivity() {

    private lateinit var progressDialog: CustomProgressBar
    private lateinit var bindingView: ActivityProfileEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_profile_edit)
        bindingView = setContentViewDataBinding(R.layout.activity_profile_edit)
        setTitle("Profile")
        setupUI()
    }

    private fun setupUI() {

        val name: List<String> = Awareness.loginData?.user?.username.toString().split(" ")

        bindingView.fullName.setText("" + Awareness.loginData?.user?.firstName)
        bindingView.phoneNumber.setText("" + Awareness.loginData?.user?.phoneNumber)
        bindingView.email.setText("" + Awareness.loginData?.user?.email)
        bindingView.cnic.setText("" + Awareness.loginData?.user?.cnic)
        bindingView.dateOfBirth.setText("" + Awareness.loginData?.user?.dateOfBirth)
        bindingView.gender.setText("" + Awareness.loginData?.user?.gender)

        bindingView.btUpdateDetails.setOnClickListener{
            validateAndProfileEdit()
        }


    }

    private fun validateAndProfileEdit() {
        val data = getProfileEditData()
        val validationResult = validateData(data)
        if (validationResult == ValidationResult.VALID) {
                progressDialog = Utils.openProgressDialog(this)
                editUserProfile(data)
        } else {
            showError(validationResult)
        }
    }

    private fun editUserProfile(data: profileRequest) {

        var call = RetrofitConnection.getAPIClient(Awareness?.loginData?.token!!)
                                                            .profileEdit(""+Awareness.getLoginData()?.user?.id,
                                                                data)

        call.enqueue(object: Callback<profileResponse>{
            override fun onFailure(call: Call<profileResponse>, t: Throwable) {
                progressDialog.dialog.dismiss()
            }

            override fun onResponse(
                call: Call<profileResponse>,
                response: Response<profileResponse>
            ) {
                progressDialog.dialog.dismiss()
                if(response.code() == 200){
                    if(response.isSuccessful){
                        if(response.body()?.success!!){
                            Log.e("success",""+response.body()?.success)
                            goToProfileActivity()
                        }else {
                           val toast = Toast.makeText(Awareness.getAppInstance().applicationContext,
                               response.message()!!,Toast.LENGTH_SHORT)
                            toast.show()
                        }

                    }else{
                        Log.e("suceess-fail",""+response.errorBody())
                    }
                }else{
                    goToLginActivity()
                }
            }


        })

    }

    private fun goToProfileActivity(){
        startActivity(Intent(this,ProfileActivity::class.java))
        finishAffinity()
    }

    private fun validateData(data: profileRequest): ValidationResult {
        return when {
            data.fullName.isBlank() -> ValidationResult.INVALID_FIRST_NAME
            //  data.lastName.isBlank() -> INVALID_LAST_NAME
            data.userPhoneNumber.isBlank() -> ValidationResult.INVALID_PHONE
            data.userPassword.isBlank() -> ValidationResult.INVALID_PASSWORD
            !data.userPassword.contentEquals(data.userNewPassword) -> ValidationResult.PASSWORD_MISS_MATCH
            //   Patterns.EMAIL_ADDRESS.toRegex().matches(data.userEmail) -> INVALID_EMAIL
            //  data.dateOfBirth.isBlank() -> INVALID_DOB
            else -> ValidationResult.VALID
        }
    }

    private fun getProfileEditData(): profileRequest {

        val accessType = "USER"
        val cityId = "1"
        val cnic = bindingView.cnic.text.toString()
        val dateOfBirth = bindingView.dateOfBirth.text.toString()
        val fullName = bindingView.fullName.text.toString()
        val gender = bindingView.gender.text.toString()
        val oldPassword = bindingView.oldPassword.text.toString()
        val userEmail = bindingView.email.text.toString()
        val userNewPassword = bindingView.password.text.toString()
        val userPassword = bindingView.passwordConfirmation.text.toString()
        val userPhoneNumber = bindingView.phoneNumber.text.toString()

        return profileRequest(
            accessType,
            cityId,
            cnic,
            dateOfBirth,
            fullName,
            gender,
            oldPassword,
            userEmail,
            userNewPassword,
            userPassword,
            userPhoneNumber
        )
    }

    private fun showError(result: ValidationResult) {
        fun resetError() {
            bindingView.fullName.error = null
            //  bindingView.lastName.error = null
            bindingView.phoneNumber.error = null
            bindingView.password.error = null
            bindingView.passwordConfirmation.error = null
            //   bindingView.email.error = null
            //   bindingView.dateOfBirth.error = null
        }

        resetError()

        when (result) {
            ValidationResult.INVALID_FIRST_NAME -> bindingView.fullName.error = "Enter full name"
            // INVALID_LAST_NAME -> bindingView.lastName.error = "Enter last name"
            ValidationResult.INVALID_PHONE -> bindingView.phoneNumber.error = "Enter phone number"
            ValidationResult.INVALID_PASSWORD -> bindingView.password.error = "Enter a password"
            // INVALID_EMAIL -> bindingView.email.error = "Invalid email"
            ValidationResult.PASSWORD_MISS_MATCH -> bindingView.passwordConfirmation.error = "Password miss match"
            // INVALID_DOB -> bindingView.dateOfBirth.error = "Select a date"
            else -> {}
        }
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


}
