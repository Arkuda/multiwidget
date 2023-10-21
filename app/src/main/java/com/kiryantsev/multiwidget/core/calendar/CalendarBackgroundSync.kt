package com.kiryantsev.multiwidget.core.calendar

import android.content.Context
import android.provider.CalendarContract
import android.util.Log
import androidx.core.database.getStringOrNull
import com.kiryantsev.multiwidget.core.calendar.db.CalendarEventEntity
import com.kiryantsev.multiwidget.core.calendar.db.CalendarEventsDao

class CalendarBackgroundSync {
    companion object {
        suspend fun sync(context: Context, calendarEventsDao: CalendarEventsDao) {
            val contentResolver = context.contentResolver

            val titleCol = CalendarContract.Events.TITLE
            val startDateCol = CalendarContract.Events.DTSTART
            val endDateCol = CalendarContract.Events.DTEND
            val colorCol = CalendarContract.Events.CALENDAR_COLOR
            val recurDateCol = CalendarContract.Events.RDATE
            val recurRuleCol = CalendarContract.Events.RRULE

            val projection =
                arrayOf(titleCol, startDateCol, endDateCol, colorCol, recurDateCol, recurRuleCol)
            val selection = CalendarContract.Events.DELETED + " != 1"

            val cursor = contentResolver.query(
                CalendarContract.Events.CONTENT_URI,
                projection, selection, null, null
            )

            val titleColIdx = cursor!!.getColumnIndex(titleCol)
            val startDateColIdx = cursor.getColumnIndex(startDateCol)
            val endDateColIdx = cursor.getColumnIndex(endDateCol)
            val colorColIdx = cursor.getColumnIndex(colorCol)
            val recurDateColIdx = cursor.getColumnIndex(recurDateCol)
            val recurRuleIdx = cursor.getColumnIndex(recurRuleCol)


            val events = mutableListOf<CalendarEventEntity>()
            cursor.moveToNext()
            while (cursor.moveToNext()) {
                try {
                    events.add(
                        CalendarEventEntity(
                            title = cursor.getString(titleColIdx),
                            startDate = cursor.getLong(startDateColIdx),
                            endDate = cursor.getLong(endDateColIdx),
                            color = cursor.getInt(colorColIdx),
                            recurDate = cursor.getStringOrNull(recurDateColIdx),
                            recurRule = cursor.getStringOrNull(recurRuleIdx)
                        ).fixToNormalItem()
                    )
                } catch (ex: Exception) {
                    Log.e("CALENDAR_ERROR", ex.message.toString())
                    ex.printStackTrace()
                }
            }

            Log.e("FETCHEDEVENT", "___________________________________")
            events.forEach {
                Log.e("FETCHEDEVENT", it.toString())
            }

            calendarEventsDao.clearItems()
            calendarEventsDao.insertEvent(events)
            cursor.close()
        }
    }
}