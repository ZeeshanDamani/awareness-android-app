package com.corona.awareness.model.signup

data class signupRequest(

    val userPhoneNumber: String,
    val firstName: String,
    val lastName: String,
    val userEmail: String,
    val userPassword: String,
    val dateOfBirth: String,
    val cityId: Int,
    val countryId: Int,
    val cnic: String,
    val accessType: String

)