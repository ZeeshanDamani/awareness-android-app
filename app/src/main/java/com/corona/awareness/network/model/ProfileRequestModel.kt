package com.corona.awareness.network.model

data class ProfileRequestModel(
    val accessType: String,
    val cityId: String,
    val cnic: String,
    val dateOfBirth: String,
    val fullName: String,
    val gender: String,
    val userEmail: String,
    val userPhoneNumber: String
)