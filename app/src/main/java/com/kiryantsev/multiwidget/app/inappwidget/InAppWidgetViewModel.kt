package com.kiryantsev.multiwidget.app.inappwidget

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.kiryantsev.multiwidget.core.settings.SettingsEntity
import com.kiryantsev.multiwidget.core.weather.openweather.OpenWeatherClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject


@HiltViewModel
class InAppWidgetViewModel @Inject constructor(
    private val application: Application
) : ViewModel() {
    private val _state = MutableStateFlow(InAppWidgetState())
    val state: StateFlow<InAppWidgetState> = _state.asStateFlow()

    private var timer: Timer? = null

    fun init() {
        runPeriodicUpdateWeather()
    }

    fun onDestroy() = timer?.cancel()

    private fun runPeriodicUpdateWeather() {
        viewModelScope.launch {
            timer = Timer().apply {
                schedule(object : TimerTask() {
                    override fun run() {
                        val settings = SettingsEntity.load()

                        if (settings.openWeatherToken.isNotEmpty()) {
                            runBlocking {
                                val client = OpenWeatherClient()
                                val currentWeather =
                                    client.getCurrentWether(settings.userLat, settings.userLng)
                                val forecast =
                                    client.getForecast(settings.userLat, settings.userLng)
                                if (currentWeather != null) {
                                    viewModelScope.launch {
                                        _state.emit(_state.value.copy(weather = currentWeather))
                                    }
                                }
                                if (forecast != null) {
                                    Log.d("TTT", Gson().toJson(forecast))
                                    viewModelScope.launch {
                                        _state.emit(_state.value.copy(forecast = forecast))
                                    }
                                }
                            }
                        }

                    }

                }, 0, 900000) // 15 mins
            }
        }
    }

}


