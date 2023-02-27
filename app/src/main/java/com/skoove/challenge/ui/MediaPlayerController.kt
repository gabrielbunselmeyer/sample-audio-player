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

    protected fun audioSelected(url: String) {
        when (mediaPlayerState.value) {
            MediaPlayerState.Started -> pauseMediaPlayer()
            MediaPlayerState.Paused, MediaPlayerState.Initialized, MediaPlayerState.Finished -> startMediaPlayer()
            else -> {
                initializeMediaPlayer(url)
            }
        }
    }

    private fun initializeMediaPlayer(url: String) {
        try {
            mediaPlayer.setAudioAttributes(attributes)
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            _mediaPlayerState.update { MediaPlayerState.Initialized }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun startMediaPlayer() {
        try {
            mediaPlayer.start()
            _mediaPlayerState.update { MediaPlayerState.Started }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun pauseMediaPlayer() {
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
    object Initialized : MediaPlayerState()
    object Started : MediaPlayerState()
    object Paused : MediaPlayerState()
    object Finished : MediaPlayerState()
}
