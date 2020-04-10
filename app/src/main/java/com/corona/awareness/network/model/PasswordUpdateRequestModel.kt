package com.corona.awareness.network.model

data class PasswordUpdateRequestModel(
    val oldPassword: String,
    val newPassword: String
)