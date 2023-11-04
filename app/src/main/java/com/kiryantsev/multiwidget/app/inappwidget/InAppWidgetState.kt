package com.kiryantsev.multiwidget.app.inappwidget

import com.kiryantsev.multiwidget.core.weather.yaweather.dto.WeatherDataClass
import com.kwabenaberko.openweathermaplib.model.currentweather.CurrentWeather
import com.kwabenaberko.openweathermaplib.model.threehourforecast.ThreeHourForecast

data class InAppWidgetState(
    val weather : CurrentWeather? = null,
    val forecast : ThreeHourForecast? = null
)
