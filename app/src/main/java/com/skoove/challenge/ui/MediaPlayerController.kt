package com.skoove.challenge.ui

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Media player controller wrapped as a ViewModel exposing its current state
 */
abstract class MediaPlayerController : ViewModel() {

    private val mediaPlayer = MediaPlayer()

    private val _mediaPlayerState = MutableStateFlow<MediaPlayerState>(MediaPlayerState.None)
    val mediaPlayerState = _mediaPlayerState.asStateFlow()

    private val attributes =
        AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()

    init {
        mediaPlayer.setOnPreparedListener {
            _mediaPlayerState.update { MediaPlayerState.Prepared }
        }
    }

    protected fun initializeMediaPlayer(url: String) {
        try {
            mediaPlayer.setAudioAttributes(attributes)
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun audioPressed() {
        when (mediaPlayerState.value) {
            MediaPlayerState.Playing -> pauseAudio()
            MediaPlayerState.Paused, MediaPlayerState.Prepared, MediaPlayerState.Finished -> playAudio()
            MediaPlayerState.None -> { /* Do nothing as MediaPlayer is initializing. */
            }
        }
    }

    private fun playAudio() {
        try {
            mediaPlayer.start()
            _mediaPlayerState.update { MediaPlayerState.Playing }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun pauseAudio() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            _mediaPlayerState.update { MediaPlayerState.Paused }
        }
    }

    protected fun releaseMediaPlayer() {
        mediaPlayer.stop()
        mediaPlayer.release()
        _mediaPlayerState.update { MediaPlayerState.None }

    }

    protected fun seekMediaPlayer(newPosition: Int) {
        mediaPlayer.seekTo(newPosition)
    }
}

/**
 * Media player state
 *
 * @constructor Create empty Media player state
 */
sealed class MediaPlayerState {
    object None : MediaPlayerState()
    object Prepared : MediaPlayerState()
    object Playing : MediaPlayerState()
    object Paused : MediaPlayerState()
    object Finished : MediaPlayerState()
}
