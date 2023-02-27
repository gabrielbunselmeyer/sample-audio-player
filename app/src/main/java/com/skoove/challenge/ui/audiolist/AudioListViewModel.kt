package com.skoove.challenge.ui.audiolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.skoove.challenge.data.Repository
import com.skoove.challenge.ui.State
import com.skoove.challenge.utils.extension.mutate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AudioListViewModel(
    application: Application,
    private val skooveRepository: Repository,
    private val mutableState: MutableStateFlow<State>
) : AndroidViewModel(application) {

    val state = mutableState.asStateFlow()

    fun dispatch(action: AudioListActions) {
        when (action) {
            is AudioListActions.FetchAudioEntries -> {
                fetchAudioEntries()
            }

            is AudioListActions.UpdateRating -> {
                mutableState.mutate {
                    val updatedRatings =
                        audioRatings.toMutableMap().apply { this[action.title] = action.stars }
                    copy(audioRatings = updatedRatings)
                }
            }

            is AudioListActions.UpdateFavoriteAudio -> mutableState.mutate { copy(favoriteAudioTitle = action.title) }
        }
    }

    private fun fetchAudioEntries() {
        viewModelScope.launch {
            skooveRepository.getAudioEntries().data?.data?.let {
                mutableState.mutate {
                    copy(audioEntries = it)
                }
            }
        }
    }
}

sealed class AudioListActions {
    data class UpdateFavoriteAudio(val title: String) : AudioListActions()
    data class UpdateRating(val title: String, val stars: Int) : AudioListActions()
    object FetchAudioEntries : AudioListActions()
}

typealias AudioListDispatcher = (AudioListActions) -> Unit
