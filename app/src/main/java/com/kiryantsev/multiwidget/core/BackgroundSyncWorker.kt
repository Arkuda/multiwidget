package com.kiryantsev.multiwidget.core

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.glance.appwidget.updateAll
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kiryantsev.multiwidget.core.calendar.CalendarBackgroundSync
import com.kiryantsev.multiwidget.core.calendar.db.CalendarEventsDao
import com.kiryantsev.multiwidget.core.settings.SettingsEntity
import com.kiryantsev.multiwidget.core.weather.YandexWeatherApi
import com.kiryantsev.multiwidget.core.weather.db.dao.CurrentWeatherDao
import com.kiryantsev.multiwidget.core.weather.db.entities.CurrentWeatherEntity
import com.kiryantsev.multiwidget.widget.MultiWidget
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@HiltWorker
class BackgroundSyncWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParams: WorkerParameters,
    @Assisted val currentWeatherDao: CurrentWeatherDao,
    @Assisted val calendarEventsDao: CalendarEventsDao,
    @Assisted val api: YandexWeatherApi,
) : CoroutineWorker(context, workerParams) {
    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val settings = SettingsEntity.load()

                updateWeather(settings)
                CalendarBackgroundSync.sync(context, calendarEventsDao)

            } catch (ex: Exception) {
                Log.e("WeatherSyncWorker", "Have error when try update weather ${ex.message}")
                ex.printStackTrace()

                updateWidget(context)
                return@withContext Result.failure()
            }
            updateWidget(context)
            return@withContext Result.success()
        }
    }

    private suspend fun updateWeather(settings: SettingsEntity?) {
        if (settings != null && settings.yandexToken.isNotEmpty()) {
            val response = api.getWeather(
                token = settings.yandexToken,
                lat = settings.userLat.toString(),
                lon = settings.userLng.toString(),
            )
            currentWeatherDao.updateCurrentWeather(
                CurrentWeatherEntity.formApiResponse(
                    response
                )
            )
        }
    }

    private suspend fun updateWidget(context: Context) = MultiWidget().updateAll(context)

}