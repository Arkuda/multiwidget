package com.kiryantsev.multiwidget.core.weather.yaweather.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kiryantsev.multiwidget.core.weather.yaweather.dto.WeatherDataClass
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable


@Serializable
@Entity(tableName = "current_weather_entity")
data class CurrentWeatherEntity(
    @PrimaryKey val uid: Int = 0,
    val temp: Int,
    val feelsLikeTemp: Int,
    val daytime: String,
    val season: String,
    val icon: String,
    val fetchTime: Long //epochMilliseconds
) {

    companion object {
        fun formApiResponse(response: WeatherDataClass): CurrentWeatherEntity =
            CurrentWeatherEntity(
                temp = response.fact.temp,
                feelsLikeTemp = response.fact.feels_like,
                daytime = response.fact.daytime,
                season = response.fact.season,
                icon = response.fact.condition,
                fetchTime = Clock.System.now().toEpochMilliseconds()
            )
    }

    fun getFetchTimeInstant() =  Instant.fromEpochMilliseconds(fetchTime)
}