package com.kiryantsev.multiwidget.app.inappwidget

import com.kiryantsev.multiwidget.core.weather.db.entities.CurrentWeatherEntity

data class InAppWidgetState(
    val currWeather : CurrentWeatherEntity? = null
)
