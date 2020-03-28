package com.corona.awareness.configs

import android.content.Context
import android.content.SharedPreferences
import com.corona.awareness.model.User
import com.corona.awareness.model.signup.signupRequest
import com.google.gson.GsonBuilder

object AppSharedPreferences {

    lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences =
            context.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE)
    }

    fun getConfig(key: String): String? {
        return sharedPreferences?.getString(key, key)
    }

    fun <T> put(`object`: T, key: String) {
        //Convert object to JSON String.
        val jsonString = GsonBuilder().create().toJson(`object`)
        //Save that String in SharedPreferences
       // Log.e("qq " ,""+jsonString.toString())
        sharedPreferences?.edit()?.putString(key, jsonString)?.commit()
    }

     inline fun <reified T> get(key: String): T? {
        //We read JSON String which was saved.
        val value = sharedPreferences?.getString(key, null)
        //JSON String was found which means object can be read.
        //We convert this JSON String to model object. Parameter "c" (of
        //type “T” is used to cast.
        return GsonBuilder().create().fromJson(value, T::class.java)
    }

    fun saveUser(user: signupRequest) {
        val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
        editor.putString(FIRST_NAME, user.firstName)
        editor.putString(LAST_NAME, user.lastName)
        editor.putString(PHONE_NUMBER, user.userPhoneNumber)
        editor.putString(CNIC, user.cnic)
        editor.putString(PASSWORD, user.userPassword)
        editor.putString(EMAIL, user.userEmail)
        editor.putString(DOB, user.dateOfBirth)
        editor.apply()
        editor.commit()
    }

    fun getUser(): User {
        val firstName = sharedPreferences?.getString(FIRST_NAME, "")
        val lastName = sharedPreferences?.getString(LAST_NAME, "")
        val phoneNumber = sharedPreferences?.getString(PHONE_NUMBER, "")
        val cnic = sharedPreferences?.getString(CNIC, "")
        val password = sharedPreferences?.getString(PASSWORD, "")
        val email = sharedPreferences?.getString(EMAIL, "")
        val dateOfBirth = sharedPreferences?.getString(DOB, "")

        return User(
            firstName!!,
            lastName!!,
            phoneNumber!!,
            cnic!!,
            password!!,
            email!!,
            dateOfBirth!!
        )
    }

    private const val FIRST_NAME: String = "firstName"
    private const val LAST_NAME: String = "lastName"
    private const val PHONE_NUMBER: String = "phoneNumber"
    private const val CNIC: String = "cnic"
    private const val PASSWORD: String = "password"
    private const val EMAIL: String = "email"
    private const val DOB: String = "dateOfBirth"
    private const val SHARED_PREF_FILE_NAME = "AppSharedPreferences"
}