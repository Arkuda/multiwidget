package com.kiryantsev.multiwidget.core.weather.openweather

import com.kiryantsev.multiwidget.core.settings.SettingsEntity
import com.kwabenaberko.openweathermaplib.constant.Languages
import com.kwabenaberko.openweathermaplib.constant.Units
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper
import com.kwabenaberko.openweathermaplib.implementation.callback.CurrentWeatherCallback
import com.kwabenaberko.openweathermaplib.implementation.callback.ThreeHourForecastCallback
import com.kwabenaberko.openweathermaplib.model.currentweather.CurrentWeather
import com.kwabenaberko.openweathermaplib.model.threehourforecast.ThreeHourForecast
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class OpenWeatherClient {

    private val helper = OpenWeatherMapHelper(SettingsEntity.load().openWeatherToken)

    init {
        helper.setUnits(Units.METRIC)
        helper.setLanguage(Languages.RUSSIAN)
    }
    suspend fun getCurrentWether(lat: Double, lng: Double): CurrentWeather? =
        suspendCoroutine<CurrentWeather?> { continuation ->
            helper.getCurrentWeatherByGeoCoordinates(lat, lng, object : CurrentWeatherCallback {
                override fun onSuccess(currentWeather: CurrentWeather?) =
                    continuation.resume(currentWeather)

                override fun onFailure(throwable: Throwable?) =
                    continuation.resumeWithException(throwable ?: Throwable("Unknown error :)"))
            })
        }


    suspend fun getForecast(lat: Double, lng: Double): ThreeHourForecast? =
        suspendCoroutine<ThreeHourForecast?> { continuation ->
            helper.getThreeHourForecastByGeoCoordinates(
                lat,
                lng,
                object : ThreeHourForecastCallback {
                    override fun onSuccess(threeHourForecast: ThreeHourForecast?) =
                        continuation.resume(threeHourForecast)

                    override fun onFailure(throwable: Throwable?) =
                        continuation.resumeWithException(throwable ?: Throwable("Unknown error :)"))

                })
        }
}