package com.kiryantsev.multiwidget.core.settings

import com.pixplicity.easyprefs.library.Prefs

data class SettingsEntity(
    val yandexToken: String,
    val userLat: Double,
    val userLng: Double,
    val isWeatherEnabled: Boolean,
    val isCalendarEnabled: Boolean,
    val isOtpEnabled: Boolean,
    val isInAppWidgetEnabled:Boolean,
) {
    companion object {
        private const val YANDEX_TOKEN_KEY = "yandexToken"
        private const val USER_LAT_KEY = "userLat"
        private const val USER_LNG_KEY = "userLng"
        private const val IS_WEATHER_ENABLED_KEY = "isWeatherEnabled"
        private const val IS_CALENDAR_ENABLED_KEY = "isCalendarEnabled"
        private const val IS_OTP_ENABLED_KEY = "isOtpEnabled"
        private const val IS_IN_APP_WIDGET_ENABLED_KEY = "isInAppWidgetEnabled"

        fun load() = SettingsEntity(
            yandexToken = Prefs.getString(YANDEX_TOKEN_KEY, ""),
            userLat = Prefs.getDouble(USER_LAT_KEY, .0),
            userLng = Prefs.getDouble(USER_LNG_KEY, .0),
            isWeatherEnabled = Prefs.getBoolean(IS_WEATHER_ENABLED_KEY, false),
            isCalendarEnabled = Prefs.getBoolean(IS_CALENDAR_ENABLED_KEY, false),
            isOtpEnabled = Prefs.getBoolean(IS_OTP_ENABLED_KEY, false),
            isInAppWidgetEnabled = Prefs.getBoolean(IS_IN_APP_WIDGET_ENABLED_KEY, false),
        )
    }

    fun save() {
        Prefs.putString(YANDEX_TOKEN_KEY, yandexToken)
        Prefs.putDouble(USER_LAT_KEY, userLat)
        Prefs.putDouble(USER_LNG_KEY, userLng)
        Prefs.putBoolean(IS_WEATHER_ENABLED_KEY, isWeatherEnabled)
        Prefs.putBoolean(IS_CALENDAR_ENABLED_KEY, isCalendarEnabled)
        Prefs.putBoolean(IS_OTP_ENABLED_KEY, isOtpEnabled)
        Prefs.putBoolean(IS_IN_APP_WIDGET_ENABLED_KEY, isInAppWidgetEnabled)
    }
}
