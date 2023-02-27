package com.skoove.challenge.ui.audiolist

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
import com.skoove.challenge.data.response.AudioModel
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
private fun AudioList(
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

    /**
     * We don't care about specific conditions for the effect to re-run, only that navigating back
     * shouldn't re-launch it.
     */
    LaunchedEffect(Unit) {
        if (state.value.audioEntries.isEmpty()) {
            refresh()
        }
    }

    Box(Modifier.pullRefresh(pullRefreshState)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = state.value.audioEntries,
            ) { item ->

                AudioListItem(audioEntry = item,
                    rating = state.value.audioRatings[item.title] ?: 0,
                    isFavorite = state.value.favoriteAudioTitle == item.title,
                    onItemClicked = { navigateToAudioDetail(item) },
                    onFavoriteClicked = { addedAsFavorite ->
                        dispatcher(AudioListActions.UpdateFavoriteAudio(if (addedAsFavorite) item.title else ""))
                    },
                    onStarClicked = { dispatcher(AudioListActions.UpdateRating(item.title, it)) })
            }
        }

        PullRefreshIndicator(refreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}
