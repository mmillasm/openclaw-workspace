package com.wakeup.ai.di

import android.content.Context
import androidx.room.Room
import com.wakeup.ai.data.local.WakeUpDatabase
import com.wakeup.ai.data.local.dao.AlarmDao
import com.wakeup.ai.data.local.dao.ChallengeResultDao
import com.wakeup.ai.data.local.dao.UserProfileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WakeUpDatabase {
        return Room.databaseBuilder(
            context,
            WakeUpDatabase::class.java,
            "wakeup_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideAlarmDao(db: WakeUpDatabase): AlarmDao = db.alarmDao()

    @Provides
    fun provideChallengeResultDao(db: WakeUpDatabase): ChallengeResultDao = db.challengeResultDao()

    @Provides
    fun provideUserProfileDao(db: WakeUpDatabase): UserProfileDao = db.userProfileDao()
}
