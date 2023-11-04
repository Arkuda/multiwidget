package com.kiryantsev.multiwidget.app.inappwidget.widgets

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.kiryantsev.multiwidget.R


@Composable
fun WeatherIcon(modifier: Modifier = Modifier, icon: String) {
    Image(
        modifier = modifier,
        painter = painterResource(id = icon.fromOpenweatherCodeToIcon()),
        contentScale = ContentScale.Fit,
        contentDescription = "$icon"
    )
}

private fun String.fromOpenweatherCodeToIcon(): Int {
    return if (this.endsWith(suffix = "n", ignoreCase = true)) {
        //night
        when (replace("n", "").toInt()) {
            1 -> R.drawable.icons8_night_100__1_ //clear
            2 -> R.drawable.icons8_partly_cloudy_day_100//few clouds
            3 -> R.drawable.icons8_cloud_100
            4 -> R.drawable.icons8_cloud_100
            9 -> R.drawable.icons8_wet_100
            10 -> R.drawable.icons8_rain_100
            11 -> R.drawable.icons8_cloud_lightning_100
            13 -> R.drawable.icons8_snow_storm_100
            50 -> R.drawable.icons8_fog_night
            else -> R.drawable.icons8_night_100__1_
        }
    } else {
        //day
        when (replace("d", "").toInt()) {
            1 -> R.drawable.icons8_afternoon_100 //clear
            2 -> R.drawable.icons8_partly_cloudy_day_100//few clouds
            3 -> R.drawable.icons8_cloud_100
            4 -> R.drawable.icons8_cloud_100
            9 -> R.drawable.icons8_wet_100
            10 -> R.drawable.icons8_rain_100
            11 -> R.drawable.icons8_cloud_lightning_100
            13 -> R.drawable.icons8_snow_storm_100
            50 -> R.drawable.icons8_fog_day
            else -> R.drawable.icons8_afternoon_100
        }
    }
}

//
////https://icons8.com/icon/set/weather/stickers
//
//
//private fun String.mapToDrawableRes(time: Int): Int {
//    if (time > 21 || time < 6) {
//        when (this) {
//            "partly-cloudy" -> return R.drawable.icons8_partly_cloudy_day_100
//            "cloudy" -> return R.drawable.icons8_partly_cloudy_day_100
//            "overcast" -> return R.drawable.icons8_cloud_100
//            "drizzle" -> return R.drawable.icons8_rain_cloud_100
//            "light-rain" -> return R.drawable.icons8_wet_100
//            "rain" -> return R.drawable.icons8_rain_100
//            "moderate-rain" -> return R.drawable.icons8_rain_100
//            "heavy-rain" -> return R.drawable.icons8_rain_100
//            "continuous-heavy-rain" -> return R.drawable.icons8_rain_100
//            "showers" -> return R.drawable.icons8_rain_100
//            "wet-snow" -> return R.drawable.icons8_sleet_100
//            "light-snow" -> return R.drawable.icons8_snow_100
//            "snow" -> return R.drawable.icons8_snow_storm_100
//            "snow-showers" -> return R.drawable.icons8_snow_100
//            "hail" -> return R.drawable.icons8_snow_100
//            "thunderstorm" -> return R.drawable.icons8_cloud_lightning_100
//            "thunderstorm-with-rain" -> return R.drawable.icons8_storm_100
//            "thunderstorm-with-hail" -> return R.drawable.icons8_storm_100
//            else -> {
//                return R.drawable.icons8_night_100__1_
//            }
//        }
//    } else {
//        when (this) {
//            "partly-cloudy" -> return R.drawable.icons8_partly_cloudy_day_100
//            "cloudy" -> return R.drawable.icons8_partly_cloudy_day_100
//            "overcast" -> return R.drawable.icons8_cloud_100
//            "drizzle" -> return R.drawable.icons8_rain_cloud_100
//            "light-rain" -> return R.drawable.icons8_wet_100
//            "rain" -> return R.drawable.icons8_rain_100
//            "moderate-rain" -> return R.drawable.icons8_rain_100
//            "heavy-rain" -> return R.drawable.icons8_rain_100
//            "continuous-heavy-rain" -> return R.drawable.icons8_rain_100
//            "showers" -> return R.drawable.icons8_rain_100
//            "wet-snow" -> return R.drawable.icons8_sleet_100
//            "light-snow" -> return R.drawable.icons8_snow_100
//            "snow" -> return R.drawable.icons8_snow_storm_100
//            "snow-showers" -> return R.drawable.icons8_snow_100
//            "hail" -> return R.drawable.icons8_snow_100
//            "thunderstorm" -> return R.drawable.icons8_cloud_lightning_100
//            "thunderstorm-with-rain" -> return R.drawable.icons8_storm_100
//            "thunderstorm-with-hail" -> return R.drawable.icons8_storm_100
//            else -> {
//                return R.drawable.icons8_afternoon_100
//            }
//        }
//    }
//}
//
//private fun String.mapToDrawableRes(dayTime: String): Int {
//    if (dayTime == "n") {
//        when (this) {
//            "partly-cloudy" -> return R.drawable.icons8_partly_cloudy_day_100
//            "cloudy" -> return R.drawable.icons8_partly_cloudy_day_100
//            "overcast" -> return R.drawable.icons8_cloud_100
//            "drizzle" -> return R.drawable.icons8_rain_cloud_100
//            "light-rain" -> return R.drawable.icons8_wet_100
//            "rain" -> return R.drawable.icons8_rain_100
//            "moderate-rain" -> return R.drawable.icons8_rain_100
//            "heavy-rain" -> return R.drawable.icons8_rain_100
//            "continuous-heavy-rain" -> return R.drawable.icons8_rain_100
//            "showers" -> return R.drawable.icons8_rain_100
//            "wet-snow" -> return R.drawable.icons8_sleet_100
//            "light-snow" -> return R.drawable.icons8_snow_100
//            "snow" -> return R.drawable.icons8_snow_storm_100
//            "snow-showers" -> return R.drawable.icons8_snow_100
//            "hail" -> return R.drawable.icons8_snow_100
//            "thunderstorm" -> return R.drawable.icons8_cloud_lightning_100
//            "thunderstorm-with-rain" -> return R.drawable.icons8_storm_100
//            "thunderstorm-with-hail" -> return R.drawable.icons8_storm_100
//            else -> {
//                return R.drawable.icons8_night_100__1_
//            }
//        }
//    } else {
//        when (this) {
//            "partly-cloudy" -> return R.drawable.icons8_partly_cloudy_day_100
//            "cloudy" -> return R.drawable.icons8_partly_cloudy_day_100
//            "overcast" -> return R.drawable.icons8_cloud_100
//            "drizzle" -> return R.drawable.icons8_rain_cloud_100
//            "light-rain" -> return R.drawable.icons8_wet_100
//            "rain" -> return R.drawable.icons8_rain_100
//            "moderate-rain" -> return R.drawable.icons8_rain_100
//            "heavy-rain" -> return R.drawable.icons8_rain_100
//            "continuous-heavy-rain" -> return R.drawable.icons8_rain_100
//            "showers" -> return R.drawable.icons8_rain_100
//            "wet-snow" -> return R.drawable.icons8_sleet_100
//            "light-snow" -> return R.drawable.icons8_snow_100
//            "snow" -> return R.drawable.icons8_snow_storm_100
//            "snow-showers" -> return R.drawable.icons8_snow_100
//            "hail" -> return R.drawable.icons8_snow_100
//            "thunderstorm" -> return R.drawable.icons8_cloud_lightning_100
//            "thunderstorm-with-rain" -> return R.drawable.icons8_storm_100
//            "thunderstorm-with-hail" -> return R.drawable.icons8_storm_100
//            else -> {
//                return R.drawable.icons8_afternoon_100
//            }
//        }
//    }
//}
