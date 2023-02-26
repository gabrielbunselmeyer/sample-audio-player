package com.skoove.challenge.ui.audiolist

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.skoove.challenge.data.Repository
import com.skoove.challenge.data.response.AudioModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class AudioListViewModel(application: Application, private val skooveRepository: Repository) :
    AndroidViewModel(application) {

    private val _audioEntries = MutableLiveData<List<AudioModel>>()
    val audioEntries: LiveData<List<AudioModel>>
        get() = _audioEntries

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
            _audioEntries.value = skooveRepository.getAudioEntries().data?.data
        }
    }
}

sealed class AudioListActions {
    object FetchAudioEntries : AudioListActions()
}

typealias AudioListDispatcher = (AudioListActions) -> Unit
