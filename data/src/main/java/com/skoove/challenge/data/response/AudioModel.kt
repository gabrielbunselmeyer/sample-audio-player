package com.skoove.challenge.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

data class AudioModel(
    val title: String,
    @SerializedName("audio") val source: String,
    val cover: String,
    val totalDurationMs: Int
)
