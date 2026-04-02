package com.wakeup.ai.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.wakeup.ai.data.local.WakeUpDatabase
import com.wakeup.ai.service.AlarmScheduler
import com.wakeup.ai.service.AlarmService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * BroadcastReceiver que recibe los intents de las alarmas.
 * CRÍTICO: Este es el punto de entrada cuando suena una alarma.
 * Debe ser extremadamente rápido y confiable.
 */
@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    @Inject
    lateinit var database: WakeUpDatabase

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != AlarmScheduler.ACTION_ALARM_TRIGGER) return

        val alarmId = intent.getStringExtra(AlarmScheduler.EXTRA_ALARM_ID) ?: return

        // Lanzar el foreground service INMEDIATAMENTE
        // No esperar, no hacer nada pesado aquí
        val serviceIntent = Intent(context, AlarmService::class.java).apply {
            action = AlarmService.ACTION_START_ALARM
            putExtra(AlarmScheduler.EXTRA_ALARM_ID, alarmId)
            putExtra(AlarmService.EXTRA_FROM_RECEIVER, true)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }

        // Re-programar la siguiente alarma para el próximo día activo
        // Se hace en background para no bloquear
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val alarm = database.alarmDao().getAlarmById(alarmId)
                alarm?.let {
                    // Ya se reprograma desde el service después de que el usuario complete
                }
            } catch (e: Exception) {
                // Log but don't crash
            }
        }
    }
}

/**
 * Re-schedules all enabled alarms after device boot.
 */
@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    @Inject
    lateinit var database: WakeUpDatabase

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == "android.intent.action.QUICKBOOT_POWERON" ||
            intent.action == "com.htc.intent.action.QUICKBOOT_POWERON"
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val enabledAlarms = database.alarmDao().getEnabledAlarms()
                    val domainAlarms = enabledAlarms.map { it.toDomain() }
                    alarmScheduler.rescheduleAllAlarms(domainAlarms)
                } catch (e: Exception) {
                    // Silently handle boot rescheduling failures
                }
            }
        }
    }
}
