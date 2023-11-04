package com.kiryantsev.multiwidget.app.inappwidget.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.kiryantsev.multiwidget.R
import com.kwabenaberko.openweathermaplib.model.threehourforecast.ThreeHourForecast
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun ForecastChart(
    modifier: Modifier = Modifier,
    forecast: ThreeHourForecast,
) {
    var horizontalSpaceToOneItem by remember { mutableFloatStateOf(0f) }
    var maxOfTemp by remember {
        mutableStateOf<Double?>(null)
    }
    var minOfTemp by remember {
        mutableStateOf<Double?>(null)
    }

    val textMeasurer = rememberTextMeasurer()

    val tempTextSize = 20.sp
    val textStyleForTemp = remember {
        TextStyle(
            fontSize = tempTextSize,
            color = Color.Black,
        )
    }

    val pathXPadding = 140f
    val pathYPadding = 70f

    val pointsIcons = forecast.list.map { it.weather.first().icon.fromOpenweatherCodeToIcon() }

    Canvas(modifier = modifier.fillMaxWidth()) {
        if (horizontalSpaceToOneItem == 0f) {
            horizontalSpaceToOneItem = (size.width - pathXPadding) / forecast.list.size
        }

        if (maxOfTemp == null) {
            maxOfTemp = forecast.list.maxOf { it.main.temp }
            minOfTemp = forecast.list.minOf { it.main.temp }
        }


        val halfYPadding = (pathYPadding / 2)


        val pathPoints = forecast.list.mapIndexed { i, item ->
            val startPosX =
                (pathXPadding / 2) + (horizontalSpaceToOneItem * (i + 1)) - horizontalSpaceToOneItem / 2
            val startPosY = (pathYPadding / 2) + item.main.temp.convertToRelative(
                size.height,
                maxOfTemp!!,
                minOfTemp!!
            )
            return@mapIndexed Pair(Offset(startPosX, startPosY), item)
        }

        //draw bg lines

        val bgLinesColor = Color.Black

        //vertical
        pathPoints.forEach {
            //todo write date text
            drawLine(
                color = bgLinesColor,
                start = Offset(it.first.x, 0 + halfYPadding),
                end = Offset(it.first.x, size.height - halfYPadding),
                strokeWidth = 1f
            )
        }

        //horizontal
//        val numOfHorizontalLines = 4f
//        val tempDelta = (maxOfTemp!! - minOfTemp!!).absoluteValue
//        val tempOneDelta = tempDelta / numOfHorizontalLines
//        val oneLineHeight = (size.height - pathYPadding) / numOfHorizontalLines
//        for (i in maxOfTemp!!..minOfTemp!! step tempOneDelta) {
//            val yCoord = oneLineHeight * (maxOfTemp!! /tempOneDelta).toString().toFloat()
//            drawLine(
//                color = bgLinesCwolor,
//                start = Offset((pathXPadding / 2), yCoord),
//                end = Offset(size.width - (pathXPadding / 2), yCoord)
//            )
//
//            //todo write temp text
//        }

        val numOfHorizontalLines = 5
        val oneLineHeight = (size.height - pathYPadding) / numOfHorizontalLines
        val tempOne = (maxOfTemp!! - minOfTemp!!).absoluteValue / numOfHorizontalLines

        for (i in (numOfHorizontalLines) downTo 1 step 1) {
            val yCoord = i * oneLineHeight
            drawLine(
                color = bgLinesColor,
                start = Offset((pathXPadding / 2), yCoord),
                end = Offset(size.width - (pathXPadding / 2), yCoord)
            )

            val temp = "${String.format("%.1f", (maxOfTemp!! - (i * tempOne)))}°"
            val tempMeasure = textMeasurer.measure(temp, style = textStyleForTemp)
            drawText(
                textLayoutResult = tempMeasure,
                topLeft = Offset(0f, yCoord - tempTextSize.roundToPx())
            )
        }


        //drawing path
        val path = Path()
        pathPoints.forEachIndexed { index, it ->
            if (index == 0) {
                path.moveTo(it.first.x, it.first.y)
            }
            path.lineTo(it.first.x, it.first.y)
        }

        drawPath(
            path = path, brush = SolidColor(Color.White), style = Stroke(
                width = 40f,
                miter = 0f,
                cap = StrokeCap.Round,
            )
        )

        drawPath(
            path = path, brush = SolidColor(Color.Black),
            style = Stroke(
                width = 4f,
                miter = 0f,
                cap = StrokeCap.Round,
            )
        )

        //draw icons
        pathPoints.forEachIndexed { index, it ->
            val image = pointsIcons[index]
            val imageSize = 70
            if(index % 2 == 1){
                drawImage(
                    image = image,
                    dstOffset = IntOffset((it.first.x - imageSize / 2).roundToInt(), (it.first.y - imageSize / 2).roundToInt()),
                    dstSize = IntSize(imageSize, imageSize)
                )
            }

        }

    }
}

private fun Double.convertToRelative(height: Float, maxOfTemp: Double, minOfTemp: Double): Float {
    val oneItem = height / (maxOfTemp.absoluteValue + minOfTemp.absoluteValue)
    return (this * oneItem).toFloat()
}

infix fun ClosedRange<Double>.doubleStep(step: Double): Iterable<Double> {
    require(start.isFinite())
    require(endInclusive.isFinite())
    require(step > 0.0) { "Step must be positive, was: $step." }
    val sequence = generateSequence(start) { previous ->
        if (previous == Double.POSITIVE_INFINITY) return@generateSequence null
        val next = previous + step
        if (next > endInclusive) null else next
    }
    return sequence.asIterable()
}

@Composable
private fun String.fromOpenweatherCodeToIcon(): ImageBitmap {
    val drawableRes = if (this.endsWith(suffix = "n", ignoreCase = true)) {
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

    return ImageBitmap.imageResource(id = drawableRes)
}


@Preview(name = "ForecastChart", showBackground = true)
@Composable
private fun PreviewForecastChart() {
    ForecastChart(
        modifier = Modifier.height(300.dp),
        forecast = Gson().fromJson(
            """
                {
                  "city": {
                    "coord": {
                      "lat": 53.2494,
                      "lon": 50.2105
                    },
                    "country": "RU",
                    "id": 499099,
                    "name": "Самара",
                    "population": 1134730,
                    "sunrise": 1698463724,
                    "sunset": 1698499021,
                    "timezone": 14400
                  },
                  "cnt": 40,
                  "cod": "200",
                  "list": [
                    {
                      "clouds": {
                        "all": 100
                      },
                      "dt": 1698526800,
                      "dt_txt": "2023-10-28 21:00:00",
                      "sys": {
                        "pod": "n",
                        "type": 0
                      },
                      "main": {
                        "feels_like": -1.21,
                        "grnd_level": 977,
                        "humidity": 100,
                        "pressure": 994,
                        "sea_level": 994,
                        "temp": 2.78,
                        "temp_kf": -2.09,
                        "temp_max": 4.87,
                        "temp_min": 2.78
                      },
                      "pop": 1,
                      "rain": {
                        "3h": 2.61
                      },
                      "visibility": 163,
                      "weather": [
                        {
                          "description": "небольшой дождь",
                          "icon": "10n",
                          "id": 500,
                          "main": "Rain"
                        }
                      ],
                      "wind": {
                        "deg": 138,
                        "gust": 12.56,
                        "speed": 4.57
                      }
                    },
                    {
                      "clouds": {
                        "all": 100
                      },
                      "dt": 1698537600,
                      "dt_txt": "2023-10-29 00:00:00",
                      "sys": {
                        "pod": "n",
                        "type": 0
                      },
                      "main": {
                        "feels_like": 2.04,
                        "grnd_level": 975,
                        "humidity": 100,
                        "pressure": 993,
                        "sea_level": 993,
                        "temp": 3.8,
                        "temp_kf": -2.03,
                        "temp_max": 5.83,
                        "temp_min": 3.8
                      },
                      "pop": 1,
                      "rain": {
                        "3h": 0.88
                      },
                      "visibility": 120,
                      "weather": [
                        {
                          "description": "небольшой дождь",
                          "icon": "10n",
                          "id": 500,
                          "main": "Rain"
                        }
                      ],
                      "wind": {
                        "deg": 174,
                        "gust": 6.37,
                        "speed": 1.94
                      }
                    },
                    {
                      "clouds": {
                        "all": 100
                      },
                      "dt": 1698548400,
                      "dt_txt": "2023-10-29 03:00:00",
                      "sys": {
                        "pod": "n",
                        "type": 0
                      },
                      "main": {
                        "feels_like": 2.47,
                        "grnd_level": 974,
                        "humidity": 94,
                        "pressure": 992,
                        "sea_level": 992,
                        "temp": 6.05,
                        "temp_kf": -1.64,
                        "temp_max": 7.69,
                        "temp_min": 6.05
                      },
                      "pop": 0.61,
                      "rain": {
                        "3h": 0.87
                      },
                      "visibility": 10000,
                      "weather": [
                        {
                          "description": "небольшой дождь",
                          "icon": "10n",
                          "id": 500,
                          "main": "Rain"
                        }
                      ],
                      "wind": {
                        "deg": 226,
                        "gust": 13.21,
                        "speed": 5.41
                      }
                    },
                    {
                      "clouds": {
                        "all": 100
                      },
                      "dt": 1698559200,
                      "dt_txt": "2023-10-29 06:00:00",
                      "sys": {
                        "pod": "d",
                        "type": 0
                      },
                      "main": {
                        "feels_like": 4.05,
                        "grnd_level": 975,
                        "humidity": 90,
                        "pressure": 991,
                        "sea_level": 991,
                        "temp": 7.23,
                        "temp_kf": 0,
                        "temp_max": 7.23,
                        "temp_min": 7.23
                      },
                      "pop": 1,
                      "rain": {
                        "3h": 0.81
                      },
                      "visibility": 10000,
                      "weather": [
                        {
                          "description": "небольшой дождь",
                          "icon": "10d",
                          "id": 500,
                          "main": "Rain"
                        }
                      ],
                      "wind": {
                        "deg": 226,
                        "gust": 11.53,
                        "speed": 5.18
                      }
                    },
                    {
                      "clouds": {
                        "all": 100
                      },
                      "dt": 1698570000,
                      "dt_txt": "2023-10-29 09:00:00",
                      "sys": {
                        "pod": "d",
                        "type": 0
                      },
                      "main": {
                        "feels_like": 1.67,
                        "grnd_level": 976,
                        "humidity": 90,
                        "pressure": 993,
                        "sea_level": 993,
                        "temp": 5.54,
                        "temp_kf": 0,
                        "temp_max": 5.54,
                        "temp_min": 5.54
                      },
                      "pop": 1,
                      "rain": {
                        "3h": 0.71
                      },
                      "visibility": 10000,
                      "weather": [
                        {
                          "description": "небольшой дождь",
                          "icon": "10d",
                          "id": 500,
                          "main": "Rain"
                        }
                      ],
                      "wind": {
                        "deg": 235,
                        "gust": 11.72,
                        "speed": 5.77
                      }
                    },
                    {
                      "clouds": {
                        "all": 100
                      },
                      "dt": 1698580800,
                      "dt_txt": "2023-10-29 12:00:00",
                      "sys": {
                        "pod": "d",
                        "type": 0
                      },
                      "main": {
                        "feels_like": 2.06,
                        "grnd_level": 977,
                        "humidity": 93,
                        "pressure": 994,
                        "sea_level": 994,
                        "temp": 5.85,
                        "temp_kf": 0,
                        "temp_max": 5.85,
                        "temp_min": 5.85
                      },
                      "pop": 1,
                      "rain": {
                        "3h": 0.35
                      },
                      "visibility": 10000,
                      "weather": [
                        {
                          "description": "небольшой дождь",
                          "icon": "10d",
                          "id": 500,
                          "main": "Rain"
                        }
                      ],
                      "wind": {
                        "deg": 250,
                        "gust": 11.04,
                        "speed": 5.79
                      }
                    },
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
                    },
                    {
                      "clouds": {
                        "all": 100
                      },
                      "dt": 1698602400,
                      "dt_txt": "2023-10-29 18:00:00",
                      "sys": {
                        "pod": "n",
                        "type": 0
                      },
                      "main": {
                        "feels_like": -3.35,
                        "grnd_level": 986,
                        "humidity": 89,
                        "pressure": 1003,
                        "sea_level": 1003,
                        "temp": 1.77,
                        "temp_kf": 0,
                        "temp_max": 1.77,
                        "temp_min": 1.77
                      },
                      "pop": 0.69,
                      "snow": {
                        "3h": 0.19
                      },
                      "visibility": 10000,
                      "weather": [
                        {
                          "description": "небольшой снег",
                          "icon": "13n",
                          "id": 600,
                          "main": "Snow"
                        }
                      ],
                      "wind": {
                        "deg": 289,
                        "gust": 12.74,
                        "speed": 6.33
                      }
                    }
                  ]
                }
            """.trimIndent(), ThreeHourForecast::class.java
        )
    )
}