package com.kiryantsev.multiwidget.core.weather.yaweather

import com.kiryantsev.multiwidget.core.weather.yaweather.dto.WeatherDataClass
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface YandexWeatherApi {

    @GET("v2/forecast")
    suspend fun getWeather(
        @Header("X-Yandex-API-Key") token: String,
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("lang") lang: String = "ru_RU",
    ): WeatherDataClass
}
