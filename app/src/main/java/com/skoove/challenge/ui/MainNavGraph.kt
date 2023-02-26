package com.skoove.challenge.ui

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.skoove.challenge.data.response.AudioModel
import com.skoove.challenge.ui.audiodetail.AudioDetailScreen
import com.skoove.challenge.ui.audiolist.AudioListScreen
import kotlinx.coroutines.InternalCoroutinesApi

object Destinations {
    const val ALERT_LIST_ROUTE = "alert_list"
    const val AUDIO_DETAIL_ROUTE = "audio_detail"

    object Arguments {
        const val AUDIO_TITLE = "audio_title"
    }
}

@SuppressLint("UnrememberedGetBackStackEntry")
@InternalCoroutinesApi
@Composable
fun MainNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Destinations.ALERT_LIST_ROUTE
) {
    val actions = remember(navController) { MainActions(navController) }

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {

        composable(Destinations.ALERT_LIST_ROUTE) {
            AudioListScreen(navigateToAudioDetail = { actions.navigateToAudioDetail(it) })
        }

        /**
         * Passing in complex data (I.e. a Parcelable) is not a good practice and creates some issues.
         * Ideally each screen would be getting the information from a single source of truth,
         * a state or a Repository of some kind.
         *
         * Because we don't have that for the AudioEntries, nor a single ID property, I'm just
         * passing its title as its identifier. Not a good idea for a proper application, of course.
         *
         * Even if we did try to get the Parcelable working, `getParcelable` requires API 33+, so
         * we'd need a workaround on top of a workaround.
         */
        composable(
            "${Destinations.AUDIO_DETAIL_ROUTE}/{${Destinations.Arguments.AUDIO_TITLE}}",
            arguments = listOf(navArgument(Destinations.Arguments.AUDIO_TITLE) {
                type = NavType.StringType
            })
        ) {
            AudioDetailScreen(
                audioTitle = it.arguments?.getString(Destinations.Arguments.AUDIO_TITLE) ?: ""
            )
        }

    }
}

/**
 * Models the navigation actions in the app.
 */
class MainActions(navController: NavHostController) {

    val navigateToAudioDetail: (audio: AudioModel) -> Unit = { audio ->
        navController.navigate("${Destinations.AUDIO_DETAIL_ROUTE}/${audio.title}")
    }
}
