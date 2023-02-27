package com.skoove.challenge.ui.audiodetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
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

    val audioEntry = uiState.value.audioEntries.firstOrNull { audio -> audio.title == audioTitle }
        ?: throw Exception()

    // We want this to only run once to start preparing the MediaPlayer as it takes a while.
    LaunchedEffect(true) {
        dispatcher(AudioDetailActions.InitializeMediaPlayer(audioEntry.source))
    }

    AudioDetailItem(
        audioEntry = audioEntry,
        mediaPlayerState = mediaPlayerState,
        isFavorite = audioEntry.title === uiState.value.favoriteAudioTitle,
        rating = 0,
        onStarClicked = { },
        onFavoriteClicked = { addedAsFavorite ->
            dispatcher(AudioDetailActions.UpdateFavoriteAudio(if (addedAsFavorite) audioEntry.title else ""))
        },
        onAudioControlClicked = { dispatcher(AudioDetailActions.AudioControlClicked) },
        onSliderValueChanged = { },
        updateMediaTime = { })
}
