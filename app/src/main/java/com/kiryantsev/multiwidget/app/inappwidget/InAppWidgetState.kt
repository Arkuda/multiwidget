package com.kiryantsev.multiwidget.app.inappwidget

import com.kiryantsev.multiwidget.core.weather.yaweather.dto.WeatherDataClass

data class InAppWidgetState(
    val weather : WeatherDataClass? = null
)
