package com.kiryantsev.multiwidget.core.di

import com.kiryantsev.multiwidget.core.weather.yaweather.YandexWeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WeatherProvider {

    @Singleton
    @Provides
    fun provideWeatherApi(): YandexWeatherApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.weather.yandex.ru")
            .build()
            .create(YandexWeatherApi::class.java)
    }

}