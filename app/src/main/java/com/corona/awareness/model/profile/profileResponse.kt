package com.corona.awareness.model.profile

data class profileResponse(
    val message: String,
    val responseCode: Int,
    val success: Boolean,
    val timestamp: Long
)