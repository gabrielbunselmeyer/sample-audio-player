package com.skoove.challenge.domain.model

data class SongModel(
    val title: String,
    val audio: String,
    val cover: String,
    val totalDurationMs: Long,
)
