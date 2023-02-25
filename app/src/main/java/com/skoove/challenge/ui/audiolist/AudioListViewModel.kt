package com.skoove.challenge.ui.audiolist

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.skoove.challenge.data.Repository
import com.skoove.challenge.data.response.AudioModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class AudioListViewModel(application: Application, private val skooveRepository: Repository) :
    AndroidViewModel(application) {

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
        }
    }

    private fun fetchAudioEntries() {
        viewModelScope.launch(repositoryCoroutinesExceptionHandler) {
            val audioEntries = mutableListOf<AudioModel>()

            val test = skooveRepository.getAudioEntries().data
            Log.d("MAINVIEWMODEL_ERROR", test.toString())
        }
    }
}

sealed class AudioListActions {
    object FetchAudioEntries : AudioListActions()
}

typealias AudioListDispatcher = (AudioListActions) -> Unit
