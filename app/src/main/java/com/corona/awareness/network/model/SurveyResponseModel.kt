package com.corona.awareness.network.model

import com.google.gson.annotations.SerializedName

data class SurveyResponseModel(
    @SerializedName("userSurveys")
    val userDiagnoses: List<DiagnosticResult>
)