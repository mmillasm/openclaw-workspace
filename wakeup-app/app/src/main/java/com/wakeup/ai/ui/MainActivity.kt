package com.wakeup.ai.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.wakeup.ai.service.AlarmScheduler
import com.wakeup.ai.ui.navigation.WakeUpNavHost
import com.wakeup.ai.ui.theme.WakeUpTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* handle result */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        requestPermissions()

        setContent {
            WakeUpTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    WakeUpApp()
                }
            }
        }
    }

    private fun requestPermissions() {
        // Notificación para Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        // Exact alarms para Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmScheduler.canScheduleExactAlarms()) {
                startActivity(alarmScheduler.getExactAlarmSettingsIntent())
            }
        }
    }
}

@Composable
fun WakeUpApp() {
    val navController = rememberNavController()
    WakeUpNavHost(navController = navController)
}
