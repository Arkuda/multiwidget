@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.kiryantsev.multiwidget.app.inappwidget

import android.annotation.SuppressLint
import android.os.Build
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.kiryantsev.multiwidget.R
import com.kiryantsev.multiwidget.app.Router
import com.kiryantsev.multiwidget.app.inappwidget.widgets.ClockWidget
import com.kiryantsev.multiwidget.app.inappwidget.widgets.ForecastChart
import com.kiryantsev.multiwidget.app.inappwidget.widgets.ForecastItemWidget
import com.kiryantsev.multiwidget.app.inappwidget.widgets.NowWeatherCardWidget
import com.kiryantsev.multiwidget.app.inappwidget.widgets.OnLifecycleEvent
import com.kwabenaberko.openweathermaplib.model.currentweather.CurrentWeather
import com.kwabenaberko.openweathermaplib.model.threehourforecast.ThreeHourForecast


@Composable
fun InAppWidgetScreen(
    viewModel: InAppWidgetViewModel,
    navController: NavController,
    window: Window
) {
    val state = viewModel.state.collectAsState().value
    InAppWidgetScreenImpl(
        state = state,
        window = window,
        onInitViewModel = { viewModel.init() },
        onDestroyEvent = viewModel::onDestroy,
        onOpenSettings = {
            navController.navigate(Router.SETTINGS_ROUTE)
        }
    )
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun InAppWidgetScreenImpl(
    state: InAppWidgetState,
    window: Window?,
    onInitViewModel: (LifecycleOwner) -> Unit,
    onDestroyEvent: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    Scaffold(Modifier) {

        val lifecycleOwner = LocalLifecycleOwner.current
        LaunchedEffect(Unit) {
            if (window != null) {
                hideSystemUI(window = window)
                onInitViewModel.invoke(lifecycleOwner)
            }
        }

        OnLifecycleEvent { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                onDestroyEvent.invoke()
            }
        }


        AsyncImage(
            model = "https://cdnph.upi.com/svc/sv/i/2321501089579/2017/1/15010945395977/Why-does-it-drizzle-instead-of-rain-NASA-offers-answer.jpg",
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        ClockWidget(modifier = Modifier.padding(32.dp))

        Box(Modifier.fillMaxSize()) {
            val configuration = LocalConfiguration.current

            Image(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .clickable { onOpenSettings.invoke() },
                painter = painterResource(R.drawable.baseline_app_settings_alt_24),
                contentDescription = null
            )


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.Gray.copy(alpha = 0.5f))
                    .height((configuration.screenHeightDp / 3).dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (state.weather != null) {
                    NowWeatherCardWidget(
                        modifier = Modifier.padding(start = 32.dp),
                        factWeather = state.weather
                    )
                }
                if (state.forecast != null) {
                    ForecastChart(
                        modifier = Modifier.padding(horizontal = 32.dp).height(((configuration.screenHeightDp / 3)-32*2).dp),
                        forecast = state.forecast
                    )
                }
            }


        }


    }
}


fun hideSystemUI(window: Window) {
    WindowCompat.setDecorFitsSystemWindows(window, false)

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    } else {
        window.insetsController?.apply {
            hide(WindowInsets.Type.statusBars())
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    with(WindowCompat.getInsetsController(window, window.decorView)) {
        systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        hide(WindowInsetsCompat.Type.systemBars())
    }
}

@Composable
@Preview(device = TABLET)
private fun InAppWidgetScreenImplPreview() {
    InAppWidgetScreenImpl(
        state = InAppWidgetState(
            weather = Gson().fromJson(
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
            ),
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
        ),
        window = null,
        onInitViewModel = {},
        onDestroyEvent = {},
        onOpenSettings = {}
    )
}