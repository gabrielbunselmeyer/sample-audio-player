package com.skoove.challenge.ui.audiodetail

import androidx.compose.runtime.*
import com.skoove.challenge.ui.MediaPlayerState
import com.skoove.challenge.ui.component.AudioDetailItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun AudioDetailScreen(
    audioTitle: String, audioDetailViewModel: AudioDetailViewModel = koinViewModel()
) {

    AudioDetail(
        audioTitle = audioTitle,
        uiState = audioDetailViewModel.state.collectAsState(),
        mediaPlayerState = audioDetailViewModel.mediaPlayerState.collectAsState(),
        dispatcher = audioDetailViewModel::dispatch
    )
}

@Composable
private fun AudioDetail(
    audioTitle: String,
    uiState: State<com.skoove.challenge.ui.State>,
    mediaPlayerState: State<MediaPlayerState>,
    dispatcher: AudioDetailDispatcher
) {
    val test = uiState.value.audioEntries
    val audioEntry = uiState.value.audioEntries.firstOrNull { audio -> audio.title == audioTitle }
        ?: throw Exception()

    var currentSeekerTime by remember { mutableStateOf(0f) }

    AudioDetailItem(audio = audioEntry,
        isAudioPlaying = mediaPlayerState.value == MediaPlayerState.Started,
        isFavorite = audioEntry.title === uiState.value.favoriteAudioTitle,
        playingTime = currentSeekerTime,
        duration = audioEntry.totalDurationMs,
        rating = 0,
        onStarClicked = { },
        onFavoriteClicked = { addedAsFavorite ->
            dispatcher(AudioDetailActions.UpdateFavoriteAudio(if (addedAsFavorite) audioEntry.title else ""))
        },
        onAudioSelected = { dispatcher(AudioDetailActions.SelectAudio(it)) },
        onSliderValueChanged = { currentSeekerTime = it },
        updateMediaTime = { dispatcher(AudioDetailActions.SeekInAudio(it)) })
}
