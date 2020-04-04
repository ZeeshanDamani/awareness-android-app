package com.corona.awareness.model.login

data class LoginResponseModel(
    val accessLevel: Int,
    val message: String,
    val success: Boolean,
    val token: String,
    val user: User
) {
    data class User(
        val accountNonExpired: Boolean,
        val accountNonLocked: Boolean,
        val authorities: List<String>,
        val cityId: Int?,
        val cnic: String?,
        val countryId: Any?,
        val createdDate: String,
        val credentialsNonExpired: Boolean,
        val dateOfBirth: String?,
        val email: String?,
        val enabled: Boolean,
        val expired: Boolean,
        val firstName: String,
        val gender: String?,
        val id: Int,
        val locked: Boolean,
        val new: Boolean,
        val password: String,
        val phoneNumber: String,
        val trackingAllowed: Boolean,
        val updatedDate: String,
        val user_type: Any?,
        val username: Any?,
        val profileCompleted: Boolean
    )
}