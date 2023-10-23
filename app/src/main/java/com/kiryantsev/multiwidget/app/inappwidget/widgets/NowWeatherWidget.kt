package com.kiryantsev.multiwidget.app.inappwidget.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.kiryantsev.multiwidget.core.weather.yaweather.dto.Fact


@Composable
fun NowWeatherWidget(factWeather: Fact) {
    Row {
        WeatherIcon(name = factWeather.icon, dayTime = factWeather.daytime)
        Text(text = factWeather.temp.toString())
    }
}



//@Composable
//@Preview(showBackground = true)
//private fun NowWeatherWidgetPreview(){
//    NowWeatherWidget(
//       Fact(
//
//       )
//    )
//}