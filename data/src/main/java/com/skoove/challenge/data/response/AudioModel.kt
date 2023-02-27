package com.skoove.challenge.data.response

import com.google.gson.annotations.SerializedName

data class AudioModel(
    val title: String,
    @SerializedName("audio") val source: String,
    val cover: String,
    val totalDurationMs: Int,
)
