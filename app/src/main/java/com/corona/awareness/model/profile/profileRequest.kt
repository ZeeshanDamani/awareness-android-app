package com.corona.awareness.model.profile

data class profileRequest(
    val accessType: String,
    val cityId: String,
    val cnic: String,
    val dateOfBirth: String,
    val fullName: String,
    val gender: String,
    val oldPassword: String,
    val userEmail: String,
    val userNewPassword: String,
    val userPassword: String,
    val userPhoneNumber: String
)