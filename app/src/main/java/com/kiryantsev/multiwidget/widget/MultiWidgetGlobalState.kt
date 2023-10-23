package com.kiryantsev.multiwidget.widget

import com.kiryantsev.multiwidget.core.calendar.db.CalendarEventEntity
import com.kiryantsev.multiwidget.core.weather.yaweather.db.entities.CurrentWeatherEntity
import kotlinx.serialization.Serializable


@Serializable
data class MultiWidgetGlobalState(
    val state: MultiWidgetGlobalStateEnum,
    val isWeatherEnabled : Boolean = false,
    val isCalendarEnabled: Boolean = false,
    val weather: CurrentWeatherEntity? = null,
    val calendarEvents: List<CalendarEventEntity> = mutableListOf(),
    val error : String? = null
)

enum class MultiWidgetGlobalStateEnum {
    NEED_CONFIGURE, ERROR, OK, LOADING
}