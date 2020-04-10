package com.corona.awareness.network.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class DiagnosticResult(
    @SerializedName("recommendation")
    val recommendation: Recommendation,
    @SerializedName("critical")
    val status: Status,
    val trackingAllowed: Boolean,
    @SerializedName("assessmentTime")
    val assessmentTime: Date
)