package com.kiryantsev.multiwidget.app.settingsscreen.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiryantsev.multiwidget.R
import com.kiryantsev.multiwidget.core.theme.MultiWidgetTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherSettingsWidget(
    yaToken: String,
    openWeatherToken: String,
    posLat: String,
    posLng: String,
    locationPermissionIsGranted: Boolean,
    onRequestPermission: () -> Unit,
    onYaTokenChanged: (String) -> Unit,
    onOpenWeatherTokenChanged: (String) -> Unit,
    onChangedPosition: (String, String) -> Unit,
    onTryAutomaticGetPosition: () -> Unit,
    locationFetchingInProgress: Boolean,
) {
    Column(
        modifier = Modifier.padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.weather_settings_yandex_token_title))
        Spacer(Modifier.height(8.dp))
        TextField(
            value = yaToken,
            onValueChange = onYaTokenChanged,
        )
        Text(stringResource(R.string.weather_settings_openweather_token_title))
        Spacer(Modifier.height(8.dp))
        TextField(
            value = openWeatherToken,
            onValueChange = onOpenWeatherTokenChanged,
        )
        Spacer(Modifier.height(8.dp))
        Text(stringResource(R.string.weather_settings_user_pos))
        Spacer(Modifier.height(8.dp))
        Row {
            TextField(
                modifier = Modifier.weight(1f),
                value = posLat.toString(),
                label = { Text(stringResource(R.string.weather_settings_user_pos_lat)) },
                onValueChange = {
                    onChangedPosition(it, posLng.toString())
                },
            )
            Spacer(Modifier.padding(start = 8.dp))
            TextField(
                modifier = Modifier.weight(1f),
                value = posLng.toString(),
                label = { Text(stringResource(R.string.weather_settings_user_pos_lng)) },
                onValueChange = {
                    onChangedPosition(posLat.toString(), it)

                },
            )
        }
        Spacer(Modifier.height(16.dp))
        if (!locationPermissionIsGranted) {
            Text(
                stringResource(R.string.weather_settings_loc_permission_title),
                textAlign = TextAlign.Center
            )
            Button(onClick = onRequestPermission) {
                Text(stringResource(R.string.weather_settings_grant_permission))
            }
        } else {
            Spacer(Modifier.padding(start = 8.dp))
            if (locationFetchingInProgress) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp))
            } else {
                Button(onClick = onTryAutomaticGetPosition) {
                    Text(stringResource(R.string.weather_settings_user_pos_auto_action))
                }
            }
        }
    }

}


@Composable
@Preview(showBackground = true)
private fun WeatherSettingsWidgetPreviewDontHave() {
    MultiWidgetTheme {
        WeatherSettingsWidget(
            "TEST_TOKEN",
            "TESTOPENWEATHER",
            posLat = "",
            posLng = "",
            false,
            {},
            {},
            {},
            { _, _ -> },
            {},
            false
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun WeatherSettingsWidgetPreviewHave() {
    MultiWidgetTheme {
        WeatherSettingsWidget(
            "TEST_TOKEN",
            "TESTOPENWEATHER",
            posLat = "",
            posLng = "",
            true,
            {},
            {},
            {},
            { _, _ -> },
            {},
            false
        )
    }
}