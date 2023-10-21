package com.kiryantsev.multiwidget.widget.widgets

import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import com.kiryantsev.multiwidget.R


@Composable
fun WeatherIcon(modifier: GlanceModifier = GlanceModifier, name: String, dayTime: String) {
    Image(
        modifier = modifier,
        provider = ImageProvider(name.mapToDrawableRes(dayTime)),
        contentDescription = "$name-$dayTime"
    )
}

@Composable
fun WeatherIcon(modifier: GlanceModifier = GlanceModifier, name: String, time: Int) {
    Image(
        modifier = modifier,
        provider = ImageProvider(name.mapToDrawableRes(time)),
        contentDescription = "$name-$time"
    )
}

//https://icons8.com/icon/set/weather/stickers


private fun String.mapToDrawableRes(time: Int): Int {
    if (time > 21 || time < 6) {
        when (this) {
            "partly-cloudy" -> return R.drawable.icons8_partly_cloudy_day_100
            "cloudy" -> return R.drawable.icons8_partly_cloudy_day_100
            "overcast" -> return R.drawable.icons8_cloud_100
            "drizzle" -> return R.drawable.icons8_rain_cloud_100
            "light-rain" -> return R.drawable.icons8_wet_100
            "rain" -> return R.drawable.icons8_rain_100
            "moderate-rain" -> return R.drawable.icons8_rain_100
            "heavy-rain" -> return R.drawable.icons8_rain_100
            "continuous-heavy-rain" -> return R.drawable.icons8_rain_100
            "showers" -> return R.drawable.icons8_rain_100
            "wet-snow" -> return R.drawable.icons8_sleet_100
            "light-snow" -> return R.drawable.icons8_snow_100
            "snow" -> return R.drawable.icons8_snow_storm_100
            "snow-showers" -> return R.drawable.icons8_snow_100
            "hail" -> return R.drawable.icons8_snow_100
            "thunderstorm" -> return R.drawable.icons8_cloud_lightning_100
            "thunderstorm-with-rain" -> return R.drawable.icons8_storm_100
            "thunderstorm-with-hail" -> return R.drawable.icons8_storm_100
            else -> {
                return R.drawable.icons8_night_100__1_
            }
        }
    } else {
        when (this) {
            "partly-cloudy" -> return R.drawable.icons8_partly_cloudy_day_100
            "cloudy" -> return R.drawable.icons8_partly_cloudy_day_100
            "overcast" -> return R.drawable.icons8_cloud_100
            "drizzle" -> return R.drawable.icons8_rain_cloud_100
            "light-rain" -> return R.drawable.icons8_wet_100
            "rain" -> return R.drawable.icons8_rain_100
            "moderate-rain" -> return R.drawable.icons8_rain_100
            "heavy-rain" -> return R.drawable.icons8_rain_100
            "continuous-heavy-rain" -> return R.drawable.icons8_rain_100
            "showers" -> return R.drawable.icons8_rain_100
            "wet-snow" -> return R.drawable.icons8_sleet_100
            "light-snow" -> return R.drawable.icons8_snow_100
            "snow" -> return R.drawable.icons8_snow_storm_100
            "snow-showers" -> return R.drawable.icons8_snow_100
            "hail" -> return R.drawable.icons8_snow_100
            "thunderstorm" -> return R.drawable.icons8_cloud_lightning_100
            "thunderstorm-with-rain" -> return R.drawable.icons8_storm_100
            "thunderstorm-with-hail" -> return R.drawable.icons8_storm_100
            else -> {
                return R.drawable.icons8_afternoon_100
            }
        }
    }
}

private fun String.mapToDrawableRes(dayTime: String): Int {
    if (dayTime == "n") {
        when (this) {
            "partly-cloudy" -> return R.drawable.icons8_partly_cloudy_day_100
            "cloudy" -> return R.drawable.icons8_partly_cloudy_day_100
            "overcast" -> return R.drawable.icons8_cloud_100
            "drizzle" -> return R.drawable.icons8_rain_cloud_100
            "light-rain" -> return R.drawable.icons8_wet_100
            "rain" -> return R.drawable.icons8_rain_100
            "moderate-rain" -> return R.drawable.icons8_rain_100
            "heavy-rain" -> return R.drawable.icons8_rain_100
            "continuous-heavy-rain" -> return R.drawable.icons8_rain_100
            "showers" -> return R.drawable.icons8_rain_100
            "wet-snow" -> return R.drawable.icons8_sleet_100
            "light-snow" -> return R.drawable.icons8_snow_100
            "snow" -> return R.drawable.icons8_snow_storm_100
            "snow-showers" -> return R.drawable.icons8_snow_100
            "hail" -> return R.drawable.icons8_snow_100
            "thunderstorm" -> return R.drawable.icons8_cloud_lightning_100
            "thunderstorm-with-rain" -> return R.drawable.icons8_storm_100
            "thunderstorm-with-hail" -> return R.drawable.icons8_storm_100
            else -> {
                return R.drawable.icons8_night_100__1_
            }
        }
    } else {
        when (this) {
            "partly-cloudy" -> return R.drawable.icons8_partly_cloudy_day_100
            "cloudy" -> return R.drawable.icons8_partly_cloudy_day_100
            "overcast" -> return R.drawable.icons8_cloud_100
            "drizzle" -> return R.drawable.icons8_rain_cloud_100
            "light-rain" -> return R.drawable.icons8_wet_100
            "rain" -> return R.drawable.icons8_rain_100
            "moderate-rain" -> return R.drawable.icons8_rain_100
            "heavy-rain" -> return R.drawable.icons8_rain_100
            "continuous-heavy-rain" -> return R.drawable.icons8_rain_100
            "showers" -> return R.drawable.icons8_rain_100
            "wet-snow" -> return R.drawable.icons8_sleet_100
            "light-snow" -> return R.drawable.icons8_snow_100
            "snow" -> return R.drawable.icons8_snow_storm_100
            "snow-showers" -> return R.drawable.icons8_snow_100
            "hail" -> return R.drawable.icons8_snow_100
            "thunderstorm" -> return R.drawable.icons8_cloud_lightning_100
            "thunderstorm-with-rain" -> return R.drawable.icons8_storm_100
            "thunderstorm-with-hail" -> return R.drawable.icons8_storm_100
            else -> {
                return R.drawable.icons8_afternoon_100
            }
        }
    }
}
