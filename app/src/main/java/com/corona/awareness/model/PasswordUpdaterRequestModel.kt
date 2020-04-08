package com.corona.awareness.model

data class PasswordUpdaterRequestModel(
    val oldPassword: String,
    val newPassword: String
)