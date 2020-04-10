package com.corona.awareness.network.model

data class PingRequestModel(
    val createdAt: String,
    val device: String,
    val latitude: String,
    val longitude: String,
    val userId: Int
)