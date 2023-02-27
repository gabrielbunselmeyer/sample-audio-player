package com.skoove.challenge.ui.audiodetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import org.koin.androidx.compose.koinViewModel

@Composable
fun AudioDetailScreen(
    audioTitle: String, audioDetailViewModel: AudioDetailViewModel = koinViewModel()
) {

    AudioDetail(
        audioTitle = audioTitle,
        uiState = audioDetailViewModel.state.collectAsState(),
        mediaPlayerState = audioDetailViewModel.mediaPlayerState.collectAsState(),
        mediaPlayerCurrentTime = audioDetailViewModel.mediaPlayerCurrentTime.collectAsState(initial = 0),
        dispatcher = audioDetailViewModel::dispatch
    )
}

@Composable
private fun AudioDetail(
    audioTitle: String,
    uiState: State<com.skoove.challenge.ui.State>,
    mediaPlayerState: State<MediaPlayerState>,
    mediaPlayerCurrentTime: State<Int>,
    dispatcher: AudioDetailDispatcher,
) {

    val audioEntry = uiState.value.audioEntries.firstOrNull { audio -> audio.title == audioTitle }
        ?: throw Exception()

    // We want this to only run once to start preparing the MediaPlayer as it takes a while.
    LaunchedEffect(true) {
        dispatcher(AudioDetailActions.InitializeMediaPlayer(audioEntry.source))
    }

    // Way too many parameters; should eventually move to some sort of data object.
    AudioDetailItem(
        audioEntry = audioEntry,
        mediaPlayerState = mediaPlayerState,
        mediaPlayerCurrentTime = mediaPlayerCurrentTime,
        isFavorite = audioEntry.title === uiState.value.favoriteAudioTitle,
        rating = uiState.value.audioRatings[audioEntry.title] ?: 0,
        onStarClicked = { dispatcher(AudioDetailActions.UpdateRating(audioEntry.title, it)) },
        onFavoriteClicked = { addedAsFavorite ->
            dispatcher(AudioDetailActions.UpdateFavoriteAudio(if (addedAsFavorite) audioEntry.title else ""))
        },
        onAudioControlClicked = { dispatcher(AudioDetailActions.AudioControlClicked) },
        onSliderValueChanged = { dispatcher(AudioDetailActions.SeekMediaPlayerTime(it)) },
    )
}
