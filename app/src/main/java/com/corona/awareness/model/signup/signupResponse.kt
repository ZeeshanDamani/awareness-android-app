package com.corona.awareness.model.signup

import com.google.gson.annotations.SerializedName

data class signupResponse(

    @SerializedName("timestamp") val timestamp : String,
    @SerializedName("message") val message : String,
    @SerializedName("responseCode") val responseCode : Int,
    @SerializedName("success") val success : Boolean

)