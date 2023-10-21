package com.kiryantsev.multiwidget.core.calendar.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CalendarEventsDao {

    @Query("SELECT * FROM calendar_event_entity WHERE startDate >= :startDateTime OR endDate >= :startDateTime ORDER BY startDate LIMIT 10")
    suspend fun getEventFrom(startDateTime: Long): List<CalendarEventEntity>

    @Insert
    suspend fun insertEvent(items: List<CalendarEventEntity>)

    @Query("DELETE FROM calendar_event_entity")
    suspend fun clearItems()

}