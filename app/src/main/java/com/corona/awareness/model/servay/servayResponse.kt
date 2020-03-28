package com.corona.awareness.model.servay

data class servayResponse(
    val userSurveys: List<UserSurvey>
) {
    data class UserSurvey(
        val assessmentScore: Any?,
        val assessmentTime: String,
        val id: Int,
        val latitude: Double,
        val longitude: Double,
        val trackingAllowed: Boolean,
        val userId: Int
    )
}