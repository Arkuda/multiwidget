package com.kiryantsev.multiwidget.core

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kiryantsev.multiwidget.core.calendar.db.CalendarEventEntity
import com.kiryantsev.multiwidget.core.calendar.db.CalendarEventsDao
import com.kiryantsev.multiwidget.core.weather.db.dao.CurrentWeatherDao
import com.kiryantsev.multiwidget.core.weather.db.entities.CurrentWeatherEntity

@Database(entities = [CurrentWeatherEntity::class, CalendarEventEntity::class], version = 2)
abstract class AppDb : RoomDatabase() {
    abstract val currentWeather: CurrentWeatherDao
    abstract val calendarEvents: CalendarEventsDao
}
