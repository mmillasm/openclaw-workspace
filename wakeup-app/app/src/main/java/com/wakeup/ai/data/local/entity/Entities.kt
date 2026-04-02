package com.wakeup.ai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.wakeup.ai.domain.model.Alarm
import com.wakeup.ai.domain.model.ChallengeType
import com.wakeup.ai.domain.model.UserProfile
import com.wakeup.ai.domain.model.AvgDayStats

@Entity(tableName = "alarms")
@TypeConverters(Converters::class)
data class AlarmEntity(
    @PrimaryKey val id: String,
    val hour: Int,
    val minute: Int,
    val daysOfWeek: Int,
    val isEnabled: Boolean,
    val label: String,
    val toneUri: String,
    val vibrate: Boolean,
    val challengeType: String,
    val difficultyLevel: Int,
    val isSmartWakeEnabled: Boolean,
    val photoReferencePath: String?,
    val voicePhrase: String?,
    val barcodeValue: String?,
    val createdAt: Long
) {
    fun toDomain() = Alarm(
        id = id,
        hour = hour,
        minute = minute,
        daysOfWeek = daysOfWeek,
        isEnabled = isEnabled,
        label = label,
        toneUri = toneUri,
        vibrate = vibrate,
        challengeType = ChallengeType.valueOf(challengeType),
        difficultyLevel = difficultyLevel,
        isSmartWakeEnabled = isSmartWakeEnabled,
        photoReferencePath = photoReferencePath,
        voicePhrase = voicePhrase,
        barcodeValue = barcodeValue,
        createdAt = createdAt
    )

    companion object {
        fun fromDomain(alarm: Alarm) = AlarmEntity(
            id = alarm.id,
            hour = alarm.hour,
            minute = alarm.minute,
            daysOfWeek = alarm.daysOfWeek,
            isEnabled = alarm.isEnabled,
            label = alarm.label,
            toneUri = alarm.toneUri,
            vibrate = alarm.vibrate,
            challengeType = alarm.challengeType.name,
            difficultyLevel = alarm.difficultyLevel,
            isSmartWakeEnabled = alarm.isSmartWakeEnabled,
            photoReferencePath = alarm.photoReferencePath,
            voicePhrase = alarm.voicePhrase,
            barcodeValue = alarm.barcodeValue,
            createdAt = alarm.createdAt
        )
    }
}

@Entity(tableName = "challenge_results")
@TypeConverters(Converters::class)
data class ChallengeResultEntity(
    @PrimaryKey val id: String,
    val alarmId: String,
    val challengeType: String,
    val difficultyLevel: Int,
    val startedAt: Long,
    val completedAt: Long?,
    val wasCompleted: Boolean,
    val attempts: Int,
    val dayOfWeek: Int
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: String,
    val currentStreak: Int,
    val longestStreak: Int,
    val totalAlarmsCompleted: Int,
    val totalAlarmsMissed: Int,
    val avgCompletionTimeMs: Long,
    val currentDifficultyLevel: Int,
    val challengeTypeSuccessRates: String, // JSON map
    val weekdayPatterns: String,            // JSON map
    val lastActiveDate: String
)

class Converters {
    @TypeConverter
    fun fromChallengeType(type: ChallengeType) = type.name

    @TypeConverter
    fun toChallengeType(value: String) = ChallengeType.valueOf(value)
}
