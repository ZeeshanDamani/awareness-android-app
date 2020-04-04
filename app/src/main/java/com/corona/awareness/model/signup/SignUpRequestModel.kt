package com.corona.awareness.model.signup

import com.google.gson.annotations.SerializedName

data class SignUpRequestModel(
    val userPhoneNumber: String,
    @SerializedName("firstName")
    val fullName: String,
    val userPassword: String,
    val accessType: String = "USER"
)