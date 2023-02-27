package com.skoove.challenge.ui.audiodetail

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

/**
 * Media player controller wrapped as a ViewModel exposing its current state
 */
abstract class MediaPlayerController : ViewModel() {

    private val mediaPlayer = MediaPlayer()

    private val _mediaPlayerState = MutableStateFlow<MediaPlayerState>(MediaPlayerState.None)
    val mediaPlayerState = _mediaPlayerState.asStateFlow()

    val mediaPlayerCurrentTime: Flow<Int> = flow {
        while (true) {
            if (mediaPlayerState.value == MediaPlayerState.None) {
                emit(0)
            } else {
                // For some reason currentPosition returns a wrong value at some point. At my wits end with this one.
                // https://stackoverflow.com/questions/43187087/android-mediaplayer-getcurrentposition-returns-incorrect-values
                val currentPos = mediaPlayer.currentPosition
                Log.d("MediaPlayerController", "Emitting currentPosition: $currentPos")
                emit(currentPos)
            }

            delay(50)
        }
    }

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

            // mediaPlayer.prepare is a VERY heavy, main thread blocking operation.
            // prepareAsync is also heavy, but at least doesn't block anything.
            mediaPlayer.prepareAsync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun audioControlClicked() {
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

    /**
     * The correct way of using [MediaPlayer.release] seems to be calling it whenever you finish
     * playing a given piece of media, so it doesn't hog on to the media resource.
     * BUT if you do that, it takes a very long time to create a new instance the next time you want
     * to use MediaPlayer. It's... weird.
     */
    protected fun releaseMediaPlayer() {
        mediaPlayer.stop()
        mediaPlayer.release()
        _mediaPlayerState.update { MediaPlayerState.None }

    }

    protected fun seekMediaPlayer(newPosition: Int) {
        if (mediaPlayerState.value != MediaPlayerState.None) {
            mediaPlayer.seekTo(newPosition)
        }
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
