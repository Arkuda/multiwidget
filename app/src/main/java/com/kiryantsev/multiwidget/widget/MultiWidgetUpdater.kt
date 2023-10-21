package com.kiryantsev.multiwidget.widget

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.google.gson.Gson
import com.kiryantsev.multiwidget.core.calendar.db.CalendarEventsDao
import com.kiryantsev.multiwidget.core.settings.SettingsEntity
import com.kiryantsev.multiwidget.core.weather.db.dao.CurrentWeatherDao
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class MultiWidgetUpdater(val widget: GlanceAppWidget) {


    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface MultiWidgetEntryPoint {
        fun currentWeather(): CurrentWeatherDao
        fun calendarEvents(): CalendarEventsDao
    }

    private lateinit var currentWeather: CurrentWeatherDao
    private lateinit var calendarEvents: CalendarEventsDao
    private val scope = MainScope()

    fun updateWidget(context: Context) {
        scope.launch {
            updateState(context, MultiWidgetGlobalState(MultiWidgetGlobalStateEnum.LOADING))
            val settings = SettingsEntity.load()

            if (!::currentWeather.isInitialized || !::calendarEvents.isInitialized) {
                val appContext = context.applicationContext
                val deps =
                    EntryPointAccessors.fromApplication(
                        appContext,
                        MultiWidgetEntryPoint::class.java
                    )
                currentWeather = deps.currentWeather()
                calendarEvents = deps.calendarEvents()
            }

            var resultState = MultiWidgetGlobalState(state = MultiWidgetGlobalStateEnum.LOADING)

            if (settings.isWeatherEnabled) {
                resultState = resultState.copy(weather = currentWeather.get())
            }

            if (settings.isCalendarEnabled) {
                val now = Clock.System.now().toEpochMilliseconds()
                Log.d("CALENDARSYNC", "now = $now")
                val events = calendarEvents.getEventFrom(now)
                Log.d("CALENDARSYNC", "Fetched events")
                events.forEach {
                    Log.d("CALENDARSYNC", it.toString())
                }
                resultState = resultState.copy(calendarEvents = events)
            }

            if (!settings.isCalendarEnabled && !settings.isWeatherEnabled) {
                updateState(
                    context = context,
                    state = resultState.copy(state = MultiWidgetGlobalStateEnum.NEED_CONFIGURE)
                )
            } else {
                updateState(
                    context,
                    resultState.copy(
                        state = MultiWidgetGlobalStateEnum.OK,
                        isWeatherEnabled = settings.isWeatherEnabled,
                        isCalendarEnabled = settings.isCalendarEnabled
                    )
                )
            }
        }
    }

    private suspend fun updateState(context: Context, state: MultiWidgetGlobalState) {
        val jsonState = Json.encodeToString(state)
        GlanceAppWidgetManager(context).getGlanceIds(MultiWidget::class.java).forEach {
            updateAppWidgetState(context, PreferencesGlanceStateDefinition, it) { prefs ->
                prefs.toMutablePreferences().apply {
                    this[MultiWidgetReceiver.PREF_KEY] = jsonState
                }
            }
            widget.update(context, it)
        }
    }
}