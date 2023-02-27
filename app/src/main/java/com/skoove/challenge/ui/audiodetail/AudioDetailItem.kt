package com.skoove.challenge.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.skoove.challenge.R
import com.skoove.challenge.data.response.AudioModel
import com.skoove.challenge.utils.extension.timeStampToDuration
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.coil.CoilImage

/**
 * Audio item for the detail view
 */
@Composable
fun AudioDetailItem(
    audio: AudioModel,
    isAudioPlaying: Boolean,
    isFavorite: Boolean,
    isPlayerLoaded: Boolean,
    playingTime: Float,
    duration: Int,
    rating: Int,
    onStarClicked: (rating: Int) -> Unit,
    onFavoriteClicked: (favorite: Boolean) -> Unit,
    onAudioSelected: (source: String) -> Unit,
    onSliderValueChanged: (value: Float) -> Unit,
    updateMediaTime: (updatedTime: Int) -> Unit
) {

    // We only want this to launch once.
    LaunchedEffect(true) {
        onAudioSelected(audio.audio)
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    if (isPlayerLoaded) {
                        onAudioSelected(audio.audio)
                    }
                },
            contentAlignment = Alignment.Center
        ) {

            // Audio Cover
            CoilImage(
                imageModel = audio.cover,
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

            if (isPlayerLoaded) {
                // Media Player Controller Icons
                Image(
                    painter = painterResource(id = if (isAudioPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                    contentDescription = stringResource(id = R.string.contentDescription_audio_is_favorite),
                    modifier = Modifier.size(120.dp)
                )

                // audio favorite status element
                FavoriteElement(modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                    favoriteState = isFavorite,
                    onClick = {
                        onFavoriteClicked(!isFavorite)
                    })
            } else {
                CircularProgressIndicator()
            }
        }

        Spacer(modifier = Modifier.size(32.dp))

        // Time
        Text(
            modifier = Modifier.wrapContentWidth(),
            textAlign = TextAlign.Center,
            text = "${
                playingTime.toInt().timeStampToDuration()
            } / ${duration.timeStampToDuration()}",
            color = MaterialTheme.colors.onSurface
        )

        // Audio Slider
        Slider(
            value = playingTime,
            onValueChange = {
                onSliderValueChanged(it)
            },
            valueRange = 0f..duration.toFloat(),
            onValueChangeFinished = {
                updateMediaTime((playingTime * 1000).toInt())
            },
            steps = 1000,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colors.secondary,
                activeTickColor = MaterialTheme.colors.secondary,
                inactiveTickColor = MaterialTheme.colors.onError,
            )
        )

        Spacer(modifier = Modifier.size(32.dp))

        // Rating
        RatingStars(modifier = Modifier.padding(8.dp),
            rating = rating,
            starSize = 64,
            onStarClicked = { index ->
                onStarClicked(index + 1)
            })

    }
}
