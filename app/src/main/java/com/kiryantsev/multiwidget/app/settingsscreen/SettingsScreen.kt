@file:OptIn(
    ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class
)

package com.kiryantsev.multiwidget.app.settingsscreen

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.READ_CALENDAR
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.kiryantsev.multiwidget.R
import com.kiryantsev.multiwidget.app.Router
import com.kiryantsev.multiwidget.app.settingsscreen.widgets.CalendarSettingsWidget
import com.kiryantsev.multiwidget.app.settingsscreen.widgets.WeatherSettingsWidget
import com.kiryantsev.multiwidget.core.theme.MultiWidgetTheme


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SettingsScreenImpl(
    viewModel: SettingsScreenViewModel,
    navController: NavController
) {
    val locationPermissionState = rememberPermissionState(
        ACCESS_FINE_LOCATION
    )
    val calendarPermissionState = rememberPermissionState(
        READ_CALENDAR
    )

    val state = viewModel.state.collectAsState().value
    val context = LocalContext.current

    SettingsScreenImpl(
        state = state,
        haveLocationPermission = locationPermissionState.status == PermissionStatus.Granted,
        haveCalendarPermission = calendarPermissionState.status == PermissionStatus.Granted,
        onSaveClick = viewModel::onSaveClick,
        onRequestCalendarPermission = calendarPermissionState::launchPermissionRequest,
        onRequestLocationPermission = locationPermissionState::launchPermissionRequest,
        onChangedWeatherSwitchState = viewModel::onChangedWeatherSwitchState,
        onChangedCalendarSwitchState = viewModel::onChangedCalendarSwitchState,
        onChangedChangeInAppWidgetSwitchState = viewModel::onChangedChangeInAppWidgetSwitchState,
        onChangedYandexToken = viewModel::onChangedYandexToken,
        onChangedOpenWeatherToken = viewModel::onChangedOpenWeatherToken,
        onChangedUserPos = viewModel::onChangedUserPos,
        onTryAutomaticGetPosition = {
            viewModel.onTryAutomaticGetPosition(
                LocationServices.getFusedLocationProviderClient(
                    context
                )
            )
        },
        onOpenInAppWidgetScreen = {
            navController.navigate(Router.IN_APP_WIDGET_ROUTE)
        }
    )
}


@Composable
private fun SettingsScreenImpl(
    state: SettingsScreenState,
    haveLocationPermission: Boolean,
    haveCalendarPermission: Boolean,
    onSaveClick: () -> Unit,
    onRequestCalendarPermission: () -> Unit,
    onRequestLocationPermission: () -> Unit,
    onChangedOpenWeatherToken: (String) -> Unit,
    onChangedYandexToken: (String) -> Unit,
    onChangedWeatherSwitchState: (Boolean) -> Unit,
    onChangedChangeInAppWidgetSwitchState: (Boolean) -> Unit,
    onChangedCalendarSwitchState: (Boolean, Boolean) -> Unit,
    onChangedUserPos: (String, String) -> Unit,
    onTryAutomaticGetPosition: () -> Unit,
    onOpenInAppWidgetScreen: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ListItem(
            headlineText = { Text(stringResource(R.string.settings_yandex_weather_title)) },
            trailingContent = {
                Switch(
                    checked = state.isYaWeatherEnabled,
                    onCheckedChange = onChangedWeatherSwitchState
                )
            },
        )
        if (state.isYaWeatherEnabled) {
            WeatherSettingsWidget(
                yaToken = state.yandexToken,
                openWeatherToken = state.openWeatherToken,
                locationPermissionIsGranted = haveLocationPermission,
                onRequestPermission = onRequestLocationPermission,
                onYaTokenChanged = onChangedYandexToken,
                onOpenWeatherTokenChanged = onChangedOpenWeatherToken,
                posLat = state.userLat,
                posLng = state.userLng,
                onChangedPosition = onChangedUserPos,
                onTryAutomaticGetPosition = onTryAutomaticGetPosition,
                locationFetchingInProgress = state.locationFetchingInProgress ?: false
            )
        }
        ListItem(
            headlineText = { Text(stringResource(R.string.settings_calendar_title)) },
            trailingContent = {
                Switch(
                    checked = state.isCalendarEnabled,
                    onCheckedChange = {
                        onChangedCalendarSwitchState(
                            it, haveCalendarPermission
                        )
                    }
                )
            },
        )
        if (state.isCalendarEnabled) {
            CalendarSettingsWidget(
                calendarPermissionIsGranted = haveCalendarPermission,
                onRequestPermission = onRequestCalendarPermission
            )
        }
        ListItem(
            headlineText = { Text(stringResource(R.string.settings_in_app_title)) },
            trailingContent = {
                Switch(
                    checked = state.isInAppWidgetEnabled,
                    onCheckedChange = onChangedChangeInAppWidgetSwitchState
                )
            },
        )
        if (state.isInAppWidgetEnabled) {
            Button(
                content = { Text(stringResource(R.string.settings_in_app_widget_open_widget)) },
                onClick = onOpenInAppWidgetScreen,
            )
        }
        Button(
            content = { Text(stringResource(R.string.settings_save_button)) },
            enabled = state.isSaveButtonEnabled(),
            onClick = onSaveClick
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun SettingsScreenDisabledPreview() {
    MultiWidgetTheme {
        SettingsScreenImpl(
            state = SettingsScreenState(
                yandexToken = "",
                openWeatherToken = "",
                userLat = "",
                userLng = "",
                isCalendarEnabled = false,
                isYaWeatherEnabled = false,
                isOtpEnabled = false,
                isInAppWidgetEnabled = false
            ),
            haveCalendarPermission = false,
            haveLocationPermission = false,
            onSaveClick = {},
            onChangedYandexToken = {},
            onChangedOpenWeatherToken = {},
            onRequestCalendarPermission = {},
            onRequestLocationPermission = {},
            onChangedWeatherSwitchState = {},
            onChangedCalendarSwitchState = { _, _ -> },
            onChangedChangeInAppWidgetSwitchState = {},
            onChangedUserPos = { _, _ -> },
            onTryAutomaticGetPosition = {},
            onOpenInAppWidgetScreen = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenEnableddPreview() {
    MultiWidgetTheme {
        SettingsScreenImpl(
            state = SettingsScreenState(
                yandexToken = "",
                openWeatherToken = "",
                userLat = "",
                userLng = "",
                isCalendarEnabled = true,
                isYaWeatherEnabled = true,
                isOtpEnabled = false,
                isInAppWidgetEnabled = true
            ),
            haveCalendarPermission = false,
            haveLocationPermission = false,
            onSaveClick = {},
            onChangedYandexToken = {},
            onChangedOpenWeatherToken = {},
            onRequestCalendarPermission = {},
            onRequestLocationPermission = {},
            onChangedWeatherSwitchState = {},
            onChangedChangeInAppWidgetSwitchState = {},
            onChangedCalendarSwitchState = { _, _ -> },
            onChangedUserPos = { _, _ -> },
            onTryAutomaticGetPosition = {},
            onOpenInAppWidgetScreen = {}
        )
    }
}