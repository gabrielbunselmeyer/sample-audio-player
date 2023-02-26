package com.skoove.challenge.ui.audiolist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.skoove.challenge.data.response.AudioModel
import com.skoove.challenge.ui.component.AudioListItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun AudioListScreen(
    navigateToAudioDetail: (audio: AudioModel) -> Unit,
    audioListViewModel: AudioListViewModel = koinViewModel()
) {
    AudioList(
        navigateToAudioDetail,
        audioListViewModel.state.collectAsState(),
        audioListViewModel::dispatch
    )
}

@Composable
fun AudioList(
    navigateToAudioDetail: (audio: AudioModel) -> Unit,
    state: State<com.skoove.challenge.ui.State>,
    dispatcher: AudioListDispatcher
) {
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }
    fun refresh() = refreshScope.launch {
        refreshing = true
        dispatcher(AudioListActions.FetchAudioEntries)

        // Delay is needed for the animation to complete.
        delay(1000)
        refreshing = false
    }

    val pullRefreshState = rememberPullRefreshState(refreshing, ::refresh)

    LaunchedEffect(
        true
    ) {
        refresh()
    }

    Box(Modifier.pullRefresh(pullRefreshState)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.Cyan
                )
        ) {
            items(
                items = state.value.audioEntries,
            ) { item ->
                AudioListItem(audio = item,
                    onItemClicked = { navigateToAudioDetail(item) },
                    onFavoriteClicked = { addedAsFavorite ->
                        dispatcher(AudioListActions.UpdateFavoriteAudio(if (addedAsFavorite) item.title else ""))
                    })
            }
        }

        PullRefreshIndicator(refreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}
