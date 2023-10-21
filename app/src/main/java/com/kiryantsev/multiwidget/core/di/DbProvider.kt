package com.kiryantsev.multiwidget.core.di

import android.content.Context
import androidx.room.Room
import com.kiryantsev.multiwidget.core.AppDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DbProvider {

    @Singleton
    @Provides
    fun provideAppDb(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(app, AppDb::class.java,"app_db").build()

    @Singleton
    @Provides
    fun provideCurrentWeatherDao(db: AppDb) = db.currentWeather

    @Singleton
    @Provides
    fun provideCalendarEventsDao(db: AppDb) = db.calendarEvents
}