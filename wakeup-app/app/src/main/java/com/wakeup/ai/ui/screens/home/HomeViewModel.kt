package com.wakeup.ai.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wakeup.ai.data.local.dao.AlarmDao
import com.wakeup.ai.data.local.dao.UserProfileDao
import com.wakeup.ai.data.local.entity.AlarmEntity
import com.wakeup.ai.domain.model.Alarm
import com.wakeup.ai.service.AlarmScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val alarmDao: AlarmDao,
    private val userProfileDao: UserProfileDao,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        alarmDao.getAllAlarms(),
        userProfileDao.getUserProfile()
    ) { alarms, profile ->
        HomeUiState(
            alarms = alarms.map { it.toDomain() },
            currentStreak = profile?.currentStreak ?: 0,
            longestStreak = profile?.longestStreak ?: 0,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )

    fun toggleAlarm(alarm: Alarm) {
        viewModelScope.launch {
            val updated = alarm.copy(isEnabled = !alarm.isEnabled)
            alarmDao.updateAlarm(AlarmEntity.fromDomain(updated))
            alarmScheduler.scheduleAlarm(updated)
        }
    }

    fun deleteAlarm(alarmId: String) {
        viewModelScope.launch {
            alarmScheduler.cancelAlarm(alarmId)
            alarmDao.deleteAlarmById(alarmId)
        }
    }
}

data class HomeUiState(
    val alarms: List<Alarm> = emptyList(),
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val isLoading: Boolean = true
)
