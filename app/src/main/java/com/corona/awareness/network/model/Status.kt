package com.corona.awareness.network.model

import com.google.gson.annotations.SerializedName

enum class Status(val code: Int) {
    @SerializedName("1")
    Critical(1),
    @SerializedName("2")
    Low(2),
    @SerializedName("3")
    Neutral(3)
}