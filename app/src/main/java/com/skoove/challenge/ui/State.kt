package com.skoove.challenge.ui

import com.skoove.challenge.data.response.AudioModel

data class State(
    val audioEntries: List<AudioModel> = emptyList(),
    val audioRatings: Map<String, Int> = emptyMap(),
    val favoriteAudioTitle: String = "",
)
