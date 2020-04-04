package com.corona.awareness.model.record.request

data class PingRequestModel(
    val createdAt: String,
    val device: String,
    val latitude: String,
    val longitude: String,
    val userId: Int
)