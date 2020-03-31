package com.corona.awareness.model.signup

data class signupRequest(

    val userPhoneNumber: String,
    val fullName: String,
    val userPassword: String,
    val cityId: Int,
    val countryId: Int,
    val accessType: String

)