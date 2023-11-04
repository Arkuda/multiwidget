package com.kiryantsev.multiwidget.app.inappwidget.widgets

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.kwabenaberko.openweathermaplib.model.currentweather.CurrentWeather


@Composable
fun NowWeatherCardWidget(modifier: Modifier = Modifier, factWeather: CurrentWeather) {
    Card(
        modifier = modifier,
        shape = CardDefaults.elevatedShape,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            val weather = factWeather.weather.first()

            Text(
                text = "${factWeather.name}, ${weather.description}",
                style = TextStyle(fontSize = 32.sp)
            )
            Row {
                Text(
                    text = factWeather.main.temp.toTempStr(),
                    style = TextStyle(fontSize = 62.sp)
                )
                WeatherIcon(icon = weather.icon, modifier = Modifier.size(76.dp))
            }

            Text(
                text = "Ощущается: ${factWeather.main.feelsLike.toTempStr()}",
                style = TextStyle(fontSize = 32.sp)
            )
        }
    }


}

private fun Double.toTempStr(): String {
    return if (this > 0) {
        "+$this°"
    } else {
        "$this°"
    }
}

/*

 */

@Composable
@Preview(showBackground = true)
private fun NowWeatherWidgetPreview() {
    NowWeatherCardWidget(
       factWeather =  Gson().fromJson(
            """
          {
            "base": "stations",
            "clouds": {
              "all": 100
            },
            "cod": 200,
            "coord": {
              "lat": 53.2174,
              "lon": 50.1943
            },
            "dt": 1698089686,
            "id": 499099,
            "main": {
              "feels_like": 4.35,
              "grnd_level": 988,
              "humidity": 80,
              "pressure": 1005,
              "sea_level": 1005,
              "temp": 7.67,
              "temp_max": 7.9,
              "temp_min": 7.67
            },
            "name": "Самара",
            "rain": {
              "1h": 0.1
            },
            "sys": {
              "country": "RU",
              "id": 49376,
              "sunrise": 1698031158,
              "sunset": 1698067659,
              "type": 2
            },
            "timezone": 14400,
            "visibility": 10000,
            "weather": [
              {
                "description": "пасмурно",
                "icon": "04n",
                "id": 804,
                "main": "Clouds"
              }
            ],
            "wind": {
              "deg": 210,
              "gust": 14.25,
              "speed": 5.83
            }
          }
          
      """.trimIndent(), CurrentWeather::class.java
        )
    )
}