package com.corona.awareness.model.signup

data class SignUpResponseModel(
    val timestamp: String,
    val message: String,
    val responseCode: Int,
    val success: Boolean
)