package com.corona.awareness.model

import com.corona.awareness.network.model.Status

object StatusMapper {
    fun map(status: Status): String {
        return when(status) {
            Status.Critical -> "Critical"
            Status.Low -> "Low risk"
            Status.Neutral -> "Neutral"
        }
    }

    fun mapColor(status: Status): String {
        return when(status) {
            Status.Critical -> "#FF1F00"
            Status.Low -> "#009F23"
            Status.Neutral -> "#69767B"
        }
    }
}