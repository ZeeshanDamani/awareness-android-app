package com.corona.awareness.activities

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Patterns
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.corona.awareness.Awareness
import com.corona.awareness.R
import com.corona.awareness.activities.ProfileEditActivity.ValidationResult.*
import com.corona.awareness.configs.AppSharedPreferences
import com.corona.awareness.databinding.ActivityProfileEditBinding
import com.corona.awareness.helper.Constants
import com.corona.awareness.network.RetrofitConnection
import com.corona.awareness.network.model.City
import com.corona.awareness.network.model.LoginResponseModel
import com.corona.awareness.network.model.ProfileRequestModel
import com.corona.awareness.network.model.User
import com.corona.awareness.setTextIfAny
import com.google.android.material.snackbar.Snackbar
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ProfileEditActivity : BaseActivity() {

    private var progressDialog: ProgressDialog? = null
    private lateinit var bindingView: ActivityProfileEditBinding
    private var cities: List<City> = emptyList()
    private var cityAdapter: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView = setContentViewDataBinding(R.layout.activity_profile_edit)
        setUpToolBar()
        setupUI()
    }

    override fun onDestroy() {
        super.onDestroy()
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

    private fun resetProgressDialog() {
        progressDialog?.dismiss()
        progressDialog = null
    }

    private fun setupUI() {

        val loginResponseModel: LoginResponseModel? =
            AppSharedPreferences.get(Constants.LOGIN_OBJECT)
        val user = loginResponseModel!!.user

        bindingView.fullName.setText(user.firstName)
        bindingView.phoneNumber.setText(user.phoneNumber)
        bindingView.email.setTextIfAny(user.email)
        bindingView.dateOfBirth.apply {
            customSelectionActionModeCallback = object : ActionMode.Callback {
                override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean =
                    false

                override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean = false

                override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false

                override fun onDestroyActionMode(mode: ActionMode?) {
                }
            }
            setTextIfAny(user.dateOfBirth)
        }
        bindingView.cnic.setTextIfAny(user.cnic)
        setGender(user.gender)
        setCity(user.cityId)

        bindingView.dateOfBirth.apply {

            fun setDate(year: Int, month: Int, day: Int) {
                setText("$day/$month/$year")
            }

            setOnClickListener {

                val calendar = Calendar.getInstance()
                val currentYear = calendar.get(Calendar.YEAR) // current year
                val currentMonth = calendar.get(Calendar.MONTH) // current month
                val currentDay = calendar.get(Calendar.DAY_OF_MONTH) // current day

                val datePickerDialog = DatePickerDialog(
                    this@ProfileEditActivity,
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        setDate(year, month, dayOfMonth)
                    }, currentYear, currentMonth, currentDay
                )
                datePickerDialog.datePicker.maxDate = Date().time
                datePickerDialog.show()
            }
        }

        bindingView.updateDetails.setOnClickListener {
            validateAndUpdateProfile()
        }
    }

    private fun setGender(gender: String?) {
        if (!gender.isNullOrBlank()) {
            when {
                gender.equals("male", true) -> {
                    bindingView.gender.setSelection(0)
                }
                gender.equals("female", true) -> {
                    bindingView.gender.setSelection(1)
                }
                else -> {
                    bindingView.gender.setSelection(2)
                }
            }
        }
    }

    private fun setCity(cityId: Int?) {
        val listType = object : TypeToken<List<City>>() {}.type
        val listOfCities: List<City>? = AppSharedPreferences.get(Constants.CITIES, listType)
        listOfCities?.let {
            cities = it
            cityAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                cities.map { city -> city.name })
            bindingView.city.adapter = cityAdapter
            if (cityId != null) {
                val index = cities.indexOfFirst { city -> city.id == cityId }
                bindingView.city.setSelection(index)
            }
        }
    }

    private fun validateAndUpdateProfile() {
        val cnic = bindingView.cnic.text.toString().replace("-", "")
        val dateOfBirth = bindingView.dateOfBirth.text.toString()
        val fullName = bindingView.fullName.text.toString()
        val gender = bindingView.gender.selectedItem as String
        val userEmail = bindingView.email.text.toString()
        val userPhoneNumber = bindingView.phoneNumber.text.toString()
        val cityId = cities[bindingView.city.selectedItemPosition].id

        val profileData = ProfileData(
            cityId, cnic, dateOfBirth, fullName, gender, userEmail,
            userPhoneNumber
        )

        val result = validateProfileData(profileData)
        if (result == VALID) {
            progressDialog = ProgressDialog.show(
                this, "",
                "Updating account please wait", true
            )
            val requestModel = profileData.toProfileRequestModel()
            editUserProfile(requestModel)
        } else {
            showError(result)
        }
    }

    private fun showError(result: ValidationResult) {
        bindingView.fullName.error = null
        bindingView.phoneNumber.error = null
        bindingView.email.error = null
        bindingView.dateOfBirth.error = null
        bindingView.cnic.error = null

        when (result) {
            INVALID_FULL_NAME -> bindingView.fullName.error = "Please provide a valid name"
            INVALID_PHONE -> bindingView.phoneNumber.error = "Please provide a valid phone number"
            INVALID_EMAIL -> bindingView.email.error = "Please provide a valid email"
            INVALID_DOB -> bindingView.dateOfBirth.error = "Please select a date"
            INVALID_CNIC -> bindingView.cnic.error = "Please provide a valid CNIC"
            else -> {
            }
        }
    }

    private fun validateProfileData(data: ProfileData): ValidationResult {
        return when {
            data.fullName.isBlank() -> INVALID_FULL_NAME
            data.userPhoneNumber.isBlank() -> INVALID_PHONE
            data.userEmail.isNotBlank() &&
                    !Patterns.EMAIL_ADDRESS.toRegex().matches(data.userEmail) -> INVALID_EMAIL
            data.dateOfBirth.isBlank() -> INVALID_DOB
            data.cnic.isBlank() || data.cnic.length != 13 -> INVALID_CNIC
            data.gender.isBlank() -> INVALID_GENDER
            data.cityId == -1 -> INVALID_CITY
            else -> VALID
        }
    }

    private fun editUserProfile(data: ProfileRequestModel) {
        val call = RetrofitConnection.getAPIClient(Awareness.loginData?.token!!)
            .profileEdit(
                "" + Awareness.getLoginData()?.user?.id,
                data
            )

        call.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                resetProgressDialog()
                Snackbar.make(
                    bindingView.container,
                    "Unable to update profile, please try again",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(
                call: Call<User>,
                response: Response<User>
            ) {
                val userModel = response.body()
                val loginResponseModel = AppSharedPreferences.get<LoginResponseModel>(
                    Constants.LOGIN_OBJECT)
                AppSharedPreferences.put(loginResponseModel!!.copy(user = userModel!!), Constants.LOGIN_OBJECT)
                resetProgressDialog()
                Snackbar.make(
                    bindingView.container,
                    "Profile update successfully",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
    }

    private data class ProfileData(
        val cityId: Int = -1,
        val cnic: String = "",
        val dateOfBirth: String = "",
        val fullName: String = "",
        val gender: String = "",
        val userEmail: String = "",
        val userPhoneNumber: String = ""
    )

    private fun ProfileData.toProfileRequestModel(): ProfileRequestModel {
        var _cnic = ""
        cnic.apply {
            val first = this.subSequence(0, 5)
            val second = this.subSequence(5, 12)
            val last = this.subSequence(12, 13)
            _cnic = "$first-$second-$last"
        }
        return ProfileRequestModel(
            "USER",
            cityId.toString(),
            _cnic,
            dateOfBirth,
            fullName,
            gender,
            userEmail,
            userPhoneNumber
        )
    }

    private enum class ValidationResult {
        VALID,
        INVALID_FULL_NAME,
        INVALID_PHONE,
        INVALID_EMAIL,
        INVALID_DOB,
        INVALID_CITY,
        INVALID_CNIC,
        INVALID_GENDER,
    }
}
