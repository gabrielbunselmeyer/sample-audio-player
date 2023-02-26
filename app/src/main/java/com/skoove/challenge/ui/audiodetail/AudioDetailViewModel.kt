package com.skoove.challenge.ui.audiodetail

import com.skoove.challenge.ui.MediaPlayerController
import com.skoove.challenge.ui.State
import com.skoove.challenge.utils.extension.mutate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AudioDetailViewModel(
    private val mutableState: MutableStateFlow<State>
) : MediaPlayerController() {

    val state = mutableState.asStateFlow()

    fun dispatch(action: AudioDetailActions) {
        when (action) {
            is AudioDetailActions.UpdateFavoriteAudio -> mutableState.mutate {
                copy(
                    favoriteAudioTitle = action.title
                )
            }
            is AudioDetailActions.SelectAudio -> audioSelected(action.source)
            is AudioDetailActions.SeekInAudio -> seekMediaPlayer(action.time)

            AudioDetailActions.StartAudio -> {
                startMediaPlayer()
            }
            AudioDetailActions.PauseAudio -> {
                pauseMediaPlayer()
            }
            AudioDetailActions.ReleaseMediaPlayer -> TODO()

        }
    }

}

sealed class AudioDetailActions {
    data class UpdateFavoriteAudio(val title: String) : AudioDetailActions()
    data class SelectAudio(val source: String) : AudioDetailActions()
    data class SeekInAudio(val time: Int) : AudioDetailActions()
    object StartAudio : AudioDetailActions()
    object PauseAudio : AudioDetailActions()
    object ReleaseMediaPlayer : AudioDetailActions()
}

typealias AudioDetailDispatcher = (AudioDetailActions) -> Unit
