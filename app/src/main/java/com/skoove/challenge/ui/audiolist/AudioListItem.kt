package com.skoove.challenge.ui.audiolist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.skoove.challenge.R
import com.skoove.challenge.data.response.AudioModel
import com.skoove.challenge.ui.component.FavoriteElement
import com.skoove.challenge.ui.component.RatingStars
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.coil.CoilImage

/**
 * Audio item for the overview list
 */
@Composable
fun AudioListItem(
    audioEntry: AudioModel,
    rating: Int,
    isFavorite: Boolean = false,
    onFavoriteClicked: (newState: Boolean) -> Unit,
    onItemClicked: () -> Unit,
    onStarClicked: (rating: Int) -> Unit,
) {


    Column(modifier = Modifier
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .border(1.dp, MaterialTheme.colors.onBackground)
        .background(MaterialTheme.colors.surface)
        .clickable {
            onItemClicked()
        }) {

        Box {

            // Cover image
            CoilImage(
                imageModel = audioEntry.cover,
                contentDescription = stringResource(id = R.string.contentDescription_audio_cover),
                shimmerParams = ShimmerParams(
                    baseColor = MaterialTheme.colors.background,
                    highlightColor = MaterialTheme.colors.surface
                ),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(3f / 2f)
                    .fillMaxWidth()
            )

            RatingStars(modifier = Modifier.padding(8.dp),
                rating,
                onStarClicked = { onStarClicked(it + 1) })
        }

        // Title and favorite section
        Row(
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .align(Alignment.End)
                .padding(horizontal = 8.dp)
                .heightIn(64.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Title
            Text(
                modifier = Modifier.wrapContentWidth(),
                textAlign = TextAlign.Center,
                text = audioEntry.title.toString(),
                color = MaterialTheme.colors.onSurface
            )

            // Favorite Heart
            FavoriteElement(
                modifier = Modifier,
                favoriteState = isFavorite,
                onClick = { onFavoriteClicked(it) })
        }
    }
}
