package com.wakeup.ai.ui.screens.alarmeditor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wakeup.ai.data.local.WakeUpDatabase
import com.wakeup.ai.data.local.entity.AlarmEntity
import com.wakeup.ai.domain.model.Alarm
import com.wakeup.ai.domain.model.ChallengeType
import com.wakeup.ai.service.AlarmScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AlarmEditorViewModel @Inject constructor(
    private val database: WakeUpDatabase,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlarmEditorState())
    val uiState: StateFlow<AlarmEditorState> = _uiState.asStateFlow()

    private var editingAlarmId: String? = null

    fun loadAlarm(alarmId: String) {
        viewModelScope.launch {
            val alarm = database.alarmDao().getAlarmById(alarmId)?.toDomain()
            alarm?.let {
                editingAlarmId = it.id
                _uiState.update { state ->
                    state.copy(
                        hour = it.hour,
                        minute = it.minute,
                        selectedDays = it.getActiveDays().toSet(),
                        challengeType = it.challengeType,
                        label = it.label,
                        isEnabled = it.isEnabled
                    )
                }
            }
        }
    }

    fun setTime(hour: Int, minute: Int) {
        _uiState.update { it.copy(hour = hour, minute = minute) }
    }

    fun toggleDay(day: Int) {
        _uiState.update { state ->
            val newDays = if (state.selectedDays.contains(day)) {
                state.selectedDays - day
            } else {
                state.selectedDays + day
            }
            state.copy(selectedDays = newDays)
        }
    }

    fun setChallengeType(type: ChallengeType) {
        _uiState.update { it.copy(challengeType = type) }
    }

    fun setLabel(label: String) {
        _uiState.update { it.copy(label = label) }
    }

    fun setEnabled(enabled: Boolean) {
        _uiState.update { it.copy(isEnabled = enabled) }
    }

    fun saveAlarm() {
        viewModelScope.launch {
            val state = _uiState.value

            // Convert days set to bitmask
            val daysOfWeek = state.selectedDays.fold(0) { acc, day ->
                val bit = if (day == 1) 0 else day - 1
                acc or (1 shl bit)
            }

            val alarm = Alarm(
                id = editingAlarmId ?: UUID.randomUUID().toString(),
                hour = state.hour,
                minute = state.minute,
                daysOfWeek = if (daysOfWeek == 0) 127 else daysOfWeek, // all days if none selected
                isEnabled = state.isEnabled,
                label = state.label,
                challengeType = state.challengeType,
                difficultyLevel = 2
            )

            database.alarmDao().insertAlarm(AlarmEntity.fromDomain(alarm))
            alarmScheduler.scheduleAlarm(alarm)
        }
    }
}

data class AlarmEditorState(
    val hour: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
    val minute: Int = Calendar.getInstance().get(Calendar.MINUTE),
    val selectedDays: Set<Int> = setOf(
        Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    ),
    val challengeType: ChallengeType = ChallengeType.MATH,
    val label: String = "",
    val isEnabled: Boolean = true
)
