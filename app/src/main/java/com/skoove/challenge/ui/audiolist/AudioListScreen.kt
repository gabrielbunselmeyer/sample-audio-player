package com.skoove.challenge.ui.audiolist

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel

@Composable
fun AudioListScreen(audioListViewModel: AudioListViewModel = koinViewModel()) {
    AudioList(audioListViewModel::dispatch)
}

@Composable
fun AudioList(dispatcher: AudioListDispatcher) {
    Text("AudioList")

    dispatcher(AudioListActions.FetchAudioEntries)
}
