package com.kiryantsev.multiwidget.app.settingsscreen

import com.kiryantsev.multiwidget.core.settings.SettingsEntity

data class SettingsScreenState(
    val yandexToken: String,
    val openWeatherToken: String,
    val userLat: String,
    val userLng: String,
    val isYaWeatherEnabled: Boolean,
    val isCalendarEnabled: Boolean,
    val isOtpEnabled: Boolean,
    val isInAppWidgetEnabled: Boolean,
    val isHaveCalendarPermission: Boolean = false,
    val locationFetchingInProgress: Boolean? = false,
) {

    fun isSaveButtonEnabled(): Boolean {
        if (isYaWeatherEnabled && (yandexToken.isEmpty() || openWeatherToken.isEmpty()) && (userLat.isEmpty() || userLng.isEmpty())) return false
        if (isCalendarEnabled && !isHaveCalendarPermission) return false
        return true
    }

    fun toSettings() = SettingsEntity(
        yandexToken = this.yandexToken,
        openWeatherToken = this.openWeatherToken,
        userLat = this.userLat.toDouble(),
        userLng = this.userLng.toDouble(),
        isWeatherEnabled = this.isYaWeatherEnabled,
        isCalendarEnabled = this.isCalendarEnabled,
        isOtpEnabled = this.isOtpEnabled,
        isInAppWidgetEnabled = isInAppWidgetEnabled,
    )

    companion object {
        fun getFromSettings() = SettingsEntity.load().run {
            SettingsScreenState(
                yandexToken = this.yandexToken,
                openWeatherToken = this.openWeatherToken,
                userLng = this.userLng.toString(),
                userLat = this.userLat.toString(),
                isYaWeatherEnabled = this.isWeatherEnabled,
                isCalendarEnabled = this.isCalendarEnabled,
                isOtpEnabled = this.isOtpEnabled,
                isInAppWidgetEnabled = this.isInAppWidgetEnabled
            )
        }
    }
}
