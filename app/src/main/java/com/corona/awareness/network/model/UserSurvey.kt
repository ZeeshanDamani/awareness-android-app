package com.corona.awareness.network.model

data class UserSurvey(
    val assessmentScore: Any?,
    val assessmentTime: String,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val trackingAllowed: Boolean,
    val userId: Int
)