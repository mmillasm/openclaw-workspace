package com.wakeup.ai.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wakeup.ai.data.local.dao.AlarmDao
import com.wakeup.ai.data.local.dao.ChallengeResultDao
import com.wakeup.ai.data.local.dao.UserProfileDao
import com.wakeup.ai.data.local.entity.AlarmEntity
import com.wakeup.ai.data.local.entity.ChallengeResultEntity
import com.wakeup.ai.data.local.entity.Converters
import com.wakeup.ai.data.local.entity.UserProfileEntity

@Database(
    entities = [
        AlarmEntity::class,
        ChallengeResultEntity::class,
        UserProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class WakeUpDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
    abstract fun challengeResultDao(): ChallengeResultDao
    abstract fun userProfileDao(): UserProfileDao
}
