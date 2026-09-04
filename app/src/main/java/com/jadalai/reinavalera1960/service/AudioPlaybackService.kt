package com.jadalai.reinavalera1960.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.jadalai.reinavalera1960.MainActivity

class AudioPlaybackService : MediaSessionService() {

    private var mediaSession: MediaSession? = null
    private lateinit var player: ExoPlayer

    companion object {
        private const val CHANNEL_ID = "bible_audio_playback_channel"
        private const val NOTIFICATION_ID = 1001

        private val _isPlaying = MutableStateFlow(false)
        val isPlaying = _isPlaying.asStateFlow()

        private val _currentTitle = MutableStateFlow<String?>(null)
        val currentTitle = _currentTitle.asStateFlow()

        private val _currentSubtitle = MutableStateFlow<String?>(null)
        val currentSubtitle = _currentSubtitle.asStateFlow()

        const val ACTION_PLAY = "com.jadalai.reinavalera1960.ACTION_PLAY"
        const val ACTION_PAUSE = "com.jadalai.reinavalera1960.ACTION_PAUSE"
        const val ACTION_STOP = "com.jadalai.reinavalera1960.ACTION_STOP"
        const val EXTRA_AUDIO_URL = "extra_audio_url"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_SUBTITLE = "extra_subtitle"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        player = ExoPlayer.Builder(this)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(C.AUDIO_CONTENT_TYPE_SPEECH)
                    .setUsage(C.USAGE_MEDIA)
                    .build(),
                true
            )
            .build()

        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                _isPlaying.value = playing
                if (playing) {
                    startForeground(NOTIFICATION_ID, buildNotification())
                }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    _isPlaying.value = false
                    stopForeground(STOP_FOREGROUND_DETACH)
                }
            }
        })

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        mediaSession = MediaSession.Builder(this, player)
            .setSessionActivity(pendingIntent)
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        when (intent?.action) {
            ACTION_PLAY -> {
                val url = intent.getStringExtra(EXTRA_AUDIO_URL)
                val title = intent.getStringExtra(EXTRA_TITLE) ?: "Santa Biblia"
                val subtitle = intent.getStringExtra(EXTRA_SUBTITLE) ?: "Reina Valera 1960"

                _currentTitle.value = title
                _currentSubtitle.value = subtitle

                if (!url.isNullOrEmpty()) {
                    val mediaItem = MediaItem.fromUri(url)
                    player.setMediaItem(mediaItem)
                    player.prepare()
                    player.play()
                } else if (player.playbackState == Player.STATE_READY) {
                    player.play()
                }
            }
            ACTION_PAUSE -> {
                player.pause()
            }
            ACTION_STOP -> {
                player.stop()
                _isPlaying.value = false
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Reproducción de Audio Bíblico",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Controles de reproducción para la Biblia en Audio"
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(_currentTitle.value ?: "Santa Biblia RVR1960")
            .setContentText(_currentSubtitle.value ?: "Reproduciendo audio...")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
}
