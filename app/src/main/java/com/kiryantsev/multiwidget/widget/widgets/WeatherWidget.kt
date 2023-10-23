package com.kiryantsev.multiwidget.widget.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.kiryantsev.multiwidget.core.weather.yaweather.db.entities.CurrentWeatherEntity

@Composable
fun WeatherWidget(currentWeatherEntity: CurrentWeatherEntity) {
    Column(
        modifier = GlanceModifier.fillMaxHeight(),
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
        verticalAlignment = Alignment.Vertical.CenterVertically,
    ) {
        GlanceWeatherIcon(
            modifier = GlanceModifier.size(width = 64.dp, height = 64.dp),
            name = currentWeatherEntity.icon,
            dayTime = currentWeatherEntity.daytime
        )

        Column(GlanceModifier.cornerRadius(6.dp).padding(bottom = 2.dp, end = 2.dp, start = 2.dp)
            .background(Color.White)) {
            Text(
                modifier = GlanceModifier.width((64 - 6 * 2).dp),
                text = currentWeatherEntity.temp.toTempString(),
                style = TextStyle(
                    fontSize = TextUnit(24f, TextUnitType.Sp),
                    textAlign = TextAlign.Center
                )
            )
            Text(
                modifier = GlanceModifier.width((64 - 6 * 2).dp),
                text = "(${currentWeatherEntity.feelsLikeTemp.toTempString()})",
                style = TextStyle(
                    fontSize = TextUnit(20f, TextUnitType.Sp),
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

private fun Int.toTempString(): String {
    return if (this > 0) {
        "+${toString()}"
    } else if (this < 0) {
        "-${toString()}"
    } else {
        toString()
    }
}