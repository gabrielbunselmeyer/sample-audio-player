package com.skoove.challenge.ui.audiolist

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.skoove.challenge.data.Repository
import com.skoove.challenge.ui.State
import com.skoove.challenge.utils.extension.mutate
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AudioListViewModel(
    application: Application,
    private val skooveRepository: Repository,
    private val mutableState: MutableStateFlow<State>
) : AndroidViewModel(application) {

    val state = mutableState.asStateFlow()

    private val repositoryCoroutinesExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        val errorMessage = "Something went wrong when fetching from the GitHub API!"
        throwable.printStackTrace()
        throwable.message?.let {
            Log.d("MAINVIEWMODEL_ERROR", it)
        }
    }

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
        viewModelScope.launch(repositoryCoroutinesExceptionHandler) {
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
