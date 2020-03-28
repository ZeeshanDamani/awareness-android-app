package com.corona.awareness.model

data class User(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val cnic: String,
    val password: String,
    val email: String,
    val dateOfBirth: String
)