package com.corona.awareness.model.login

data class loginResponse(
    val accessLevel: Int,
    val city: City,
    val token: String,
    val user: User,
    val success: Boolean,
    val message: String
) {
    data class City(
        val code: String,
        val id: Int,
        val name: String
    )

    data class User(
        val accountNonExpired: Boolean,
        val accountNonLocked: Boolean,
        val authorities: List<String>,
        val cityId: Int,
        val cnic: String,
        val countryId: Int,
        val credentialsNonExpired: Boolean,
        val dateOfBirth: String,
        val enabled: Boolean,
        val id: Int,
        val new: Boolean,
        val password: String,
        val firstName: String,
        val lastName: String,
        val phoneNumber: String,
        val username: Any?
    )
}