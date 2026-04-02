package com.wakeup.ai.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.wakeup.ai.ui.screens.alarmeditor.AlarmEditorScreen
import com.wakeup.ai.ui.screens.home.HomeScreen
import com.wakeup.ai.ui.screens.settings.SettingsScreen
import com.wakeup.ai.ui.screens.stats.StatsScreen

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object AlarmEditor : Screen("alarm_editor/{alarmId}") {
        fun createRoute(alarmId: String? = null) = "alarm_editor/${alarmId ?: "new"}"
    }
    data object Stats : Screen("stats")
    data object Settings : Screen("settings")
}

@Composable
fun WakeUpNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToAlarmEditor = { alarmId ->
                    navController.navigate(Screen.AlarmEditor.createRoute(alarmId))
                },
                onNavigateToStats = {
                    navController.navigate(Screen.Stats.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(
            route = Screen.AlarmEditor.route,
            arguments = listOf(
                navArgument("alarmId") {
                    type = NavType.StringType
                    defaultValue = "new"
                }
            )
        ) { backStackEntry ->
            val alarmId = backStackEntry.arguments?.getString("alarmId")
            AlarmEditorScreen(
                alarmId = if (alarmId == "new") null else alarmId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Stats.route) {
            StatsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
