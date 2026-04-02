package com.wakeup.ai.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import com.wakeup.ai.R
import com.wakeup.ai.data.local.WakeUpDatabase
import com.wakeup.ai.domain.model.ChallengeType
import com.wakeup.ai.ui.MainActivity
import com.wakeup.ai.ui.screens.challenge.ChallengeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Foreground Service que mantiene la alarma activa.
 * CRÍTICO: debe permanecer vivo hasta que el usuario complete el desafío.
 * Si se mata, la alarma se apaga y el usuario pierde.
 */
@AndroidEntryPoint
class AlarmService : Service() {

    @Inject
    lateinit var database: WakeUpDatabase

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null
    private var wakeLock: PowerManager.WakeLock? = null
    private var volumeFadeJob: Job? = null
    private var currentAlarmId: String? = null

    companion object {
        const val ACTION_START_ALARM = "com.wakeup.ai.START_ALARM"
        const val ACTION_STOP_ALARM = "com.wakeup.ai.STOP_ALARM"
        const val ACTION_DISMISS = "com.wakeup.ai.DISMISS"
        const val EXTRA_ALARM_ID = "alarm_id"
        const val EXTRA_FROM_RECEIVER = "from_receiver"
        const val EXTRA_CHALLENGE_TYPE = "challenge_type"

        private const val CHANNEL_ID = "alarm_channel"
        private const val NOTIFICATION_ID = 1001
        private const val WAKELOCK_TAG = "WakeUp:AlarmWakeLock"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        acquireWakeLock()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_ALARM -> {
                val alarmId = intent.getStringExtra(EXTRA_ALARM_ID) ?: return START_NOT_STICKY
                currentAlarmId = alarmId
                startForeground(NOTIFICATION_ID, buildNotification())
                startAlarmSound()
                launchChallenge(alarmId)
            }
            ACTION_DISMISS -> {
                dismissAlarm(completed = true)
            }
            ACTION_STOP_ALARM -> {
                dismissAlarm(completed = false)
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        stopAlarmSound()
        releaseWakeLock()
        super.onDestroy()
    }

    private fun launchChallenge(alarmId: String) {
        CoroutineScope(Dispatchers.Main).launch {
            // Pequeño delay para que el servicio arranque bien
            delay(500)

            val alarm = database.alarmDao().getAlarmById(alarmId)?.toDomain()

            val challengeIntent = Intent(this@AlarmService, ChallengeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
                putExtra(EXTRA_ALARM_ID, alarmId)
                putExtra(EXTRA_CHALLENGE_TYPE, alarm?.challengeType?.name ?: ChallengeType.MATH.name)
                putExtra(EXTRA_FROM_RECEIVER, true)
            }
            startActivity(challengeIntent)
        }
    }

    private fun startAlarmSound() {
        try {
            val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                setDataSource(this@AlarmService, alarmUri)
                isLooping = true
                prepare()
                start()
            }

            // Gradual volume increase (0 → 100% en 10 segundos)
            volumeFadeJob = CoroutineScope(Dispatchers.IO).launch {
                var volume = 0f
                while (volume < 1f && mediaPlayer?.isPlaying == true) {
                    mediaPlayer?.setVolume(volume, volume)
                    volume += 0.05f
                    delay(500)
                }
            }
        } catch (e: Exception) {
            // Fallback si no puede reproducir
        }

        // Vibración
        startVibration()
    }

    private fun startVibration() {
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        val pattern = longArrayOf(0, 500, 200, 500, 200, 500)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createWaveform(pattern, 0))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(pattern, 0)
        }
    }

    private fun stopAlarmSound() {
        volumeFadeJob?.cancel()
        try {
            mediaPlayer?.apply {
                if (isPlaying) stop()
                release()
            }
        } catch (e: Exception) { }
        mediaPlayer = null

        vibrator?.cancel()
    }

    private fun dismissAlarm(completed: Boolean) {
        stopAlarmSound()

        CoroutineScope(Dispatchers.IO).launch {
            // Reprogramar la siguiente alarma para este alarmId
            currentAlarmId?.let { alarmId ->
                val alarm = database.alarmDao().getAlarmById(alarmId)?.toDomain()
                alarm?.let {
                    alarmScheduler.scheduleAlarm(it)
                }
            }
        }

        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    fun onChallengeCompleted() {
        dismissAlarm(completed = true)
    }

    fun onChallengeFailed() {
        dismissAlarm(completed = false)
    }

    private fun acquireWakeLock() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            WAKELOCK_TAG
        ).apply {
            acquire(30 * 60 * 1000L) // Max 30 minutos
        }
    }

    private fun releaseWakeLock() {
        try {
            if (wakeLock?.isHeld == true) {
                wakeLock?.release()
            }
        } catch (e: Exception) { }
        wakeLock = null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Alarmas",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificación persistente de alarma"
                setSound(null, null)
                enableVibration(true)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(): Notification {
        val dismissIntent = Intent(this, AlarmService::class.java).apply {
            action = ACTION_DISMISS
        }
        val dismissPi = PendingIntent.getService(
            this, 0, dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val openIntent = Intent(this, ChallengeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            currentAlarmId?.let { putExtra(EXTRA_ALARM_ID, it) }
        }
        val openPi = PendingIntent.getActivity(
            this, 1, openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentTitle("⏰ ¡Despierta!")
            .setContentText("Completa el desafío para apagar la alarma")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setFullScreenIntent(openPi, true)
            .addAction(0, "Completar desafío", dismissPi)
            .build()
    }
}
