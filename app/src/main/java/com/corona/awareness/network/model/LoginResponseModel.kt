package com.corona.awareness.network.model

data class LoginResponseModel(
    val accessLevel: Int,
    val message: String,
    val success: Boolean,
    val token: String,
    val user: User
)