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

            is AudioDetailActions.InitializeMediaPlayer -> initializeMediaPlayer(action.source)
            AudioDetailActions.AudioControlClicked -> audioControlClicked()
        }
    }

}

sealed class AudioDetailActions {
    data class UpdateFavoriteAudio(val title: String) : AudioDetailActions()
    data class InitializeMediaPlayer(val source: String) : AudioDetailActions()
    object AudioControlClicked : AudioDetailActions()
}

typealias AudioDetailDispatcher = (AudioDetailActions) -> Unit
