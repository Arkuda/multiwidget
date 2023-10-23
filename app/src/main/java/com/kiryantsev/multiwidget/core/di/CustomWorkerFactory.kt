package com.kiryantsev.multiwidget.core.di

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.kiryantsev.multiwidget.core.workers.BackgroundSyncWorker
import com.kiryantsev.multiwidget.core.calendar.db.CalendarEventsDao
import com.kiryantsev.multiwidget.core.weather.yaweather.YandexWeatherApi
import com.kiryantsev.multiwidget.core.weather.yaweather.db.dao.CurrentWeatherDao
import com.kiryantsev.multiwidget.core.workers.ForegroundSyncWorker
import javax.inject.Inject


class CustomWorkerFactory @Inject constructor(
    private val api: YandexWeatherApi,
    private val currentWeatherDao: CurrentWeatherDao,
    private val calendarEventsDao : CalendarEventsDao
) : WorkerFactory() {

    @SuppressLint("RestrictedApi")
    val defaultFactory = getDefaultWorkerFactory();

    @SuppressLint("RestrictedApi")
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? {
        if (workerClassName == BackgroundSyncWorker::class.java.name) {
            return BackgroundSyncWorker(
                context = appContext,
                workerParams = workerParameters,
                currentWeatherDao = currentWeatherDao,
                api = api,
                calendarEventsDao = calendarEventsDao,
            )
        }else if(workerClassName == ForegroundSyncWorker::class.java.name) {
            return ForegroundSyncWorker(
                context = appContext,
                workerParams = workerParameters,
                currentWeatherDao = currentWeatherDao,
                api = api,
                calendarEventsDao = calendarEventsDao,
            )
        }else {
            return defaultFactory.createWorker(appContext, workerClassName, workerParameters)
        }
    }
}