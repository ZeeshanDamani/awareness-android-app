package com.corona.awareness.configs

import android.content.Context
import android.content.SharedPreferences
import com.corona.awareness.model.User

object AppSharedPreferences {

    private lateinit var sharedPreferences: SharedPreferences;

    fun init(context: Context) {
        sharedPreferences =
            context.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE)
    }

    fun getConfig(key: String): String? {
        return sharedPreferences.getString(key, key)
    }

    fun saveUser(user: User) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(FIRST_NAME, user.firstName)
        editor.putString(LAST_NAME, user.lastName)
        editor.putString(PHONE_NUMBER, user.phoneNumber)
        editor.putString(CNIC, user.cnic)
        editor.putString(PASSWORD, user.password)
        editor.putString(EMAIL, user.email)
        editor.putString(DOB, user.dateOfBirth)
        editor.apply()
        editor.commit()
    }

    fun getUser(): User {
        val firstName = sharedPreferences.getString(FIRST_NAME, "")
        val lastName = sharedPreferences.getString(LAST_NAME, "")
        val phoneNumber = sharedPreferences.getString(PHONE_NUMBER, "")
        val cnic = sharedPreferences.getString(CNIC, "")
        val password = sharedPreferences.getString(PASSWORD, "")
        val email = sharedPreferences.getString(EMAIL, "")
        val dateOfBirth = sharedPreferences.getString(DOB, "")

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