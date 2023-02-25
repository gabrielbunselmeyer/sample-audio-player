package com.skoove.challenge.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * Audio object
 */
@Serializable
@Parcelize
data class AudioEntry(val title: String,
                      val audio: String,
                      val cover: String,
                      val totalDurationMs: Int): Parcelable