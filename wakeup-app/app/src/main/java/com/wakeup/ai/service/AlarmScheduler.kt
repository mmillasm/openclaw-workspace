package com.wakeup.ai.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import com.wakeup.ai.domain.model.Alarm
import com.wakeup.ai.receiver.AlarmReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Programador de alarmas exactas.
 * Maneja todas las complejidades de AlarmManager, Doze mode,
 * y permisos de Android 12+.
 */
@Singleton
class AlarmScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    companion object {
        const val ACTION_ALARM_TRIGGER = "com.wakeup.ai.ALARM_TRIGGER"
        const val EXTRA_ALARM_ID = "alarm_id"
        const val EXTRA_CHALLENGE_TYPE = "challenge_type"
        private const val REQUEST_CODE_BASE = 100000
    }

    /**
     * Programa una alarma para la próxima ocurrencia válida.
     * Si el tiempo ya pasó hoy, programa para mañana.
     */
    fun scheduleAlarm(alarm: Alarm) {
        if (!alarm.isEnabled) {
            cancelAlarm(alarm.id)
            return
        }

        val triggerTime = calculateNextTriggerTime(alarm)
        if (triggerTime <= System.currentTimeMillis()) {
            return // Ya pasó, se reprograma desde el receiver
        }

        val pendingIntent = createPendingIntent(alarm)

        // Configurar AlarmManager según la API level
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                scheduleExact(alarmManager, triggerTime, pendingIntent)
            } else {
                // Fallback a inexacta si no hay permiso
                scheduleInexact(alarmManager, triggerTime, pendingIntent)
            }
        } else {
            scheduleExact(alarmManager, triggerTime, pendingIntent)
        }
    }

    /**
     * Programa TODAS las alarmas activas (usado en BootReceiver).
     */
    suspend fun rescheduleAllAlarms(alarms: List<Alarm>) {
        alarms.forEach { scheduleAlarm(it) }
    }

    /**
     * Cancela una alarma específica.
     */
    fun cancelAlarm(alarmId: String) {
        val pendingIntent = createPendingIntent(alarmId)
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    /**
     * Cancela TODAS las alarmas (usado en tests).
     */
    fun cancelAllAlarms() {
        alarmManager.cancelAll()
    }

    /**
     * Verifica si la app puede programar alarmas exactas.
     * Útil para mostrarle al usuario que debe activar el permiso.
     */
    fun canScheduleExactAlarms(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    /**
     * Retorna la URI para abrir la pantalla de configuración de AlarmManager.
     */
    fun getExactAlarmSettingsIntent(): android.content.Intent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
        } else {
            Intent(Settings.ACTION_APPLICATION_SETTINGS)
        }
    }

    private fun scheduleExact(am: AlarmManager, triggerAt: Long, pi: PendingIntent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAt,
                pi
            )
        } else {
            am.setExact(AlarmManager.RTC_WAKEUP, triggerAt, pi)
        }
    }

    private fun scheduleInexact(am: AlarmManager, triggerAt: Long, pi: PendingIntent) {
        am.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAt,
            pi
        )
    }

    private fun calculateNextTriggerTime(alarm: Alarm): Long {
        val now = Calendar.getInstance()
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, alarm.hour)
            set(Calendar.MINUTE, alarm.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // Si ya pasó hoy, empezar desde mañana
        if (calendar.timeInMillis <= now.timeInMillis) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        // Buscar el próximo día activo
        var daysChecked = 0
        while (daysChecked < 7) {
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            // Calendar.SUNDAY=1, our bitmask usa 1=Sunday
            if (alarm.isActiveOnDay(dayOfWeek)) {
                return calendar.timeInMillis
            }
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            daysChecked++
        }

        // Fallback: mañana
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        return calendar.timeInMillis
    }

    private fun createPendingIntent(alarm: Alarm): PendingIntent {
        return createPendingIntent(alarm.id)
    }

    private fun createPendingIntent(alarmId: String): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = ACTION_ALARM_TRIGGER
            putExtra(EXTRA_ALARM_ID, alarmId)
            setPackage(context.packageName)
        }

        val requestCode = (REQUEST_CODE_BASE + alarmId.hashCode()).and(0xFFFF)

        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                PendingIntent.FLAG_IMMUTABLE or
                PendingIntent.FLAG_NO_CREATE
        ) ?: PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
