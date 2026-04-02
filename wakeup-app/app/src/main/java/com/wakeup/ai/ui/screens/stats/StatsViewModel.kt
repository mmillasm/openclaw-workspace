package com.wakeup.ai.ui.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wakeup.ai.data.local.dao.ChallengeResultDao
import com.wakeup.ai.data.local.dao.UserProfileDao
import com.wakeup.ai.data.local.entity.ChallengeResultEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val userProfileDao: UserProfileDao,
    private val challengeResultDao: ChallengeResultDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    init {
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            val profile = userProfileDao.getUserProfileOnce()
            val recentResults = challengeResultDao.getRecentResults(20)
            val avgTime = challengeResultDao.getAvgCompletionTime() ?: 0L

            // Collect flow
            recentResults.collect { results ->
                _uiState.value = StatsUiState(
                    currentStreak = profile?.currentStreak ?: 0,
                    longestStreak = profile?.longestStreak ?: 0,
                    totalCompleted = profile?.totalAlarmsCompleted ?: 0,
                    totalMissed = profile?.totalAlarmsMissed ?: 0,
                    avgCompletionTimeMs = avgTime,
                    recentResults = results
                )
            }
        }
    }
}

data class StatsUiState(
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalCompleted: Int = 0,
    val totalMissed: Int = 0,
    val avgCompletionTimeMs: Long = 0,
    val recentResults: List<ChallengeResultEntity> = emptyList()
)
