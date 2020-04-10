package com.corona.awareness.network.model

data class SignUpResponseModel(
    val timestamp: String,
    val message: String,
    val responseCode: Int,
    val success: Boolean
)