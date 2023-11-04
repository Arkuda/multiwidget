package com.kiryantsev.multiwidget.app.inappwidget.widgets

import android.graphics.drawable.shapes.Shape
import android.text.format.Time
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
import com.kwabenaberko.openweathermaplib.model.threehourforecast.ThreeHourForecastWeather


@Composable
fun ForecastItemWidget(modifier: Modifier = Modifier, forecast: ThreeHourForecastWeather) {

    val time = Time().apply { set(forecast.dt * 1000) }
    Card(
        modifier = modifier,
        shape = CardDefaults.elevatedShape,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            val weather = forecast.weather.first()

            Text(
                text = "${time.monthDay} ${time.month} ${time.hour}:${time.minute}",
                style = TextStyle(fontSize = 32.sp)
            )
            Row {
                Text(
                    text = forecast.main.temp.toTempStr(),
                    style = TextStyle(fontSize = 62.sp)
                )
                WeatherIcon(icon = weather.icon, modifier = Modifier.size(76.dp))
            }

            Text(
                text = "Ощущается: ${forecast.main.feelsLike.toTempStr()}",
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
private fun ForecastItemWidgetPreview() {
    ForecastItemWidget(forecast = Gson().fromJson("""
        {
      "clouds": {
        "all": 100
      },
      "dt": 1698591600,
      "dt_txt": "2023-10-29 15:00:00",
      "sys": {
        "pod": "n",
        "type": 0
      },
      "main": {
        "feels_like": -2.1,
        "grnd_level": 982,
        "humidity": 92,
        "pressure": 999,
        "sea_level": 999,
        "temp": 2.71,
        "temp_kf": 0,
        "temp_max": 2.71,
        "temp_min": 2.71
      },
      "pop": 0.66,
      "rain": {
        "3h": 0.15
      },
      "visibility": 276,
      "weather": [
        {
          "description": "небольшой дождь",
          "icon": "10n",
          "id": 500,
          "main": "Rain"
        }
      ],
      "wind": {
        "deg": 280,
        "gust": 11.94,
        "speed": 6.21
      }
    }
    """.trimIndent(), ThreeHourForecastWeather::class.java))

}