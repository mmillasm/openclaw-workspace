package com.wakeup.ai.data.local.dao

import androidx.room.*
import com.wakeup.ai.data.local.entity.AlarmEntity
import com.wakeup.ai.data.local.entity.ChallengeResultEntity
import com.wakeup.ai.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarms ORDER BY hour, minute")
    fun getAllAlarms(): Flow<List<AlarmEntity>>

    @Query("SELECT * FROM alarms WHERE id = :id")
    suspend fun getAlarmById(id: String): AlarmEntity?

    @Query("SELECT * FROM alarms WHERE isEnabled = 1")
    suspend fun getEnabledAlarms(): List<AlarmEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: AlarmEntity)

    @Update
    suspend fun updateAlarm(alarm: AlarmEntity)

    @Delete
    suspend fun deleteAlarm(alarm: AlarmEntity)

    @Query("DELETE FROM alarms WHERE id = :id")
    suspend fun deleteAlarmById(id: String)

    @Query("UPDATE alarms SET isEnabled = :enabled WHERE id = :id")
    suspend fun setAlarmEnabled(id: String, enabled: Boolean)
}

@Dao
interface ChallengeResultDao {
    @Query("SELECT * FROM challenge_results WHERE alarmId = :alarmId ORDER BY startedAt DESC")
    fun getResultsForAlarm(alarmId: String): Flow<List<ChallengeResultEntity>>

    @Query("SELECT * FROM challenge_results ORDER BY startedAt DESC LIMIT :limit")
    fun getRecentResults(limit: Int = 30): Flow<List<ChallengeResultEntity>>

    @Query("SELECT * FROM challenge_results WHERE startedAt >= :startOfDay AND startedAt < :endOfDay LIMIT 1")
    suspend fun getResultForDay(startOfDay: Long, endOfDay: Long): ChallengeResultEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: ChallengeResultEntity)

    @Query("SELECT COUNT(*) FROM challenge_results WHERE wasCompleted = 1 AND startedAt >= :startTs")
    suspend fun getCompletedCountSince(startTs: Long): Int

    @Query("SELECT AVG(completedAt - startedAt) FROM challenge_results WHERE wasCompleted = 1 AND completedAt IS NOT NULL")
    suspend fun getAvgCompletionTime(): Long?
}

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 'local_user'")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 'local_user'")
    suspend fun getUserProfileOnce(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET currentStreak = :streak, longestStreak = :longest, lastActiveDate = :date WHERE id = 'local_user'")
    suspend fun updateStreak(streak: Int, longest: Int, date: String)

    @Query("UPDATE user_profile SET currentDifficultyLevel = :level WHERE id = 'local_user'")
    suspend fun updateDifficulty(level: Int)
}
