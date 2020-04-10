package com.corona.awareness.network.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class PostAnswerResponseModel(
    @SerializedName("recomendation", alternate = ["recommendation"])
    val recommendation: Recommendation,
    @SerializedName("critical")
    val status: Status,
    val trackingAllowed: Boolean,
    @SerializedName("assessmentTime")
    val assessmentTime: Date
)