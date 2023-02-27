package com.skoove.challenge.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.skoove.challenge.R
import com.skoove.challenge.data.response.AudioModel
import com.skoove.challenge.ui.MediaPlayerState
import com.skoove.challenge.utils.extension.timeStampToDuration
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.coil.CoilImage

/**
 * Audio item for the detail view
 */
@Composable
fun AudioDetailItem(
    audioEntry: AudioModel,
    mediaPlayerState: State<MediaPlayerState>,
    isFavorite: Boolean,
    rating: Int,
    onStarClicked: (rating: Int) -> Unit,
    onFavoriteClicked: (favorite: Boolean) -> Unit,
    onAudioControlClicked: (source: String) -> Unit,
    onSliderValueChanged: (value: Float) -> Unit,
) {

    val isAudioPlaying = mediaPlayerState.value == MediaPlayerState.Playing
    val isMediaPlayerLoading = mediaPlayerState.value == MediaPlayerState.None
    var currentMediaTime by remember { mutableStateOf(0f) }


    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() }, indication = null
            ) {
                onAudioControlClicked(audioEntry.source)
            },
        ) {

            CoilImage(
                imageModel = audioEntry.cover,
                contentDescription = null,
                shimmerParams = ShimmerParams(
                    baseColor = MaterialTheme.colors.background,
                    highlightColor = MaterialTheme.colors.surface
                ),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxWidth()
            )

            Image(
                painter = painterResource(id = if (isAudioPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                contentDescription = stringResource(id = R.string.contentDescription_audio_is_favorite),
                modifier = Modifier.size(120.dp)
            )

            FavoriteElement(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                favoriteState = isFavorite,
                onClick = {
                    onFavoriteClicked(!isFavorite)
                })

            if (isMediaPlayerLoading) {
                Box(
                    modifier = Modifier
                        .background(Color.Red)
                        .matchParentSize()
                )

                CircularProgressIndicator(
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.size(32.dp))

        // Time
        Text(
            modifier = Modifier.wrapContentWidth(),
            textAlign = TextAlign.Center,
            text = "${
                currentMediaTime.toInt().timeStampToDuration()
            } / ${audioEntry.totalDurationMs.timeStampToDuration()}",
            color = MaterialTheme.colors.onSurface
        )

        // Audio Slider
        Slider(
            value = currentMediaTime,
            onValueChange = { time ->
                currentMediaTime = time
                onSliderValueChanged(time)
            },
            valueRange = 0f..audioEntry.totalDurationMs.toFloat(),
            steps = 1000,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colors.secondary,
                activeTickColor = MaterialTheme.colors.secondary,
                inactiveTickColor = MaterialTheme.colors.onError,
            )
        )

        Spacer(modifier = Modifier.size(32.dp))

        RatingStars(
            modifier = Modifier.padding(8.dp),
            rating = rating,
            starSize = 64,
            onStarClicked = { index ->
                onStarClicked(index + 1)
            }
        )
    }
}
