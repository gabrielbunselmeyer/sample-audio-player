package com.skoove.challenge.ui

import com.skoove.challenge.data.response.AudioModel

/**
 * Because of the limited scope of the application, it IMO makes sense having a single state holder.
 */
data class State(
    val audioEntries: List<AudioModel> = emptyList(),

    // Very naive way of keeping track of ratings and favorites, but it works as a showcase.
    val audioRatings: Map<String, Int> = emptyMap(),
    val favoriteAudioTitle: String = "",
)
