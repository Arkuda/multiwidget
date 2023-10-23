package com.kiryantsev.multiwidget.core.workers

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.gson.Gson
import com.kiryantsev.multiwidget.core.calendar.db.CalendarEventsDao
import com.kiryantsev.multiwidget.core.settings.SettingsEntity
import com.kiryantsev.multiwidget.core.weather.yaweather.YandexWeatherApi
import com.kiryantsev.multiwidget.core.weather.yaweather.db.dao.CurrentWeatherDao
import com.kiryantsev.multiwidget.core.weather.yaweather.dto.WeatherDataClass
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ForegroundSyncWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParams: WorkerParameters,
    @Assisted val currentWeatherDao: CurrentWeatherDao,
    @Assisted val calendarEventsDao: CalendarEventsDao,
    @Assisted val api: YandexWeatherApi,
) : CoroutineWorker(context, workerParams) {

    companion object {
        val DATA_WEATHER_KEY = "wather"

        fun extractWeatherRes(data : Data ): WeatherDataClass?{
            val weatherStr = data.getString(DATA_WEATHER_KEY)
            return if(weatherStr == null){
                null
            }else {
                Gson().fromJson(weatherStr, WeatherDataClass::class.java)
            }
        }
    }


    @SuppressLint("MissingPermission", "RestrictedApi")
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val settings = SettingsEntity.load()

               val weather = updateWeather(settings)
//                CalendarBackgroundSync.sync(context, calendarEventsDao)

                return@withContext Result.success(
                    workDataOf(DATA_WEATHER_KEY to Gson().toJson(weather))
                )
            } catch (ex: Exception) {
                Log.e("ForegroundSyncWorker", "Have error ${ex.message}")
                ex.printStackTrace()

                return@withContext Result.failure()
            }
        }
    }

    private suspend fun updateWeather(settings: SettingsEntity?): WeatherDataClass? {
        if (settings != null && settings.yandexToken.isNotEmpty()) {
            return api.getWeather(
                token = settings.yandexToken,
                lat = settings.userLat.toString(),
                lon = settings.userLng.toString(),
            )
        }
        return null
    }


}