package com.kiryantsev.multiwidget.core.weather.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kiryantsev.multiwidget.core.weather.db.entities.CurrentWeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCurrentWeather(currentWeather: CurrentWeatherEntity)

    @Query("SELECT * FROM current_weather_entity where uid = 0")
    suspend fun get(): CurrentWeatherEntity?

    @Query("SELECT * FROM current_weather_entity where uid = 0")
    fun getFlow() : Flow<CurrentWeatherEntity?>
}