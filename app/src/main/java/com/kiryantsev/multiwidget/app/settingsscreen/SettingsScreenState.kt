package com.kiryantsev.multiwidget.app.settingsscreen

import com.kiryantsev.multiwidget.core.settings.SettingsEntity

data class SettingsScreenState(
    val yandexToken: String,
    val openWeatherToken: String,
    val userLat: Double,
    val userLng: Double,
    val isYaWeatherEnabled: Boolean,
    val isCalendarEnabled: Boolean,
    val isOtpEnabled: Boolean,
    val isInAppWidgetEnabled: Boolean,
    val isHaveCalendarPermission: Boolean = false,
    val locationFetchingInProgress: Boolean? = false,
) {

    fun isSaveButtonEnabled(): Boolean {
        if (isYaWeatherEnabled && (yandexToken.isEmpty() || openWeatherToken.isEmpty()) && (userLat == .0 || userLng == .0)) return false
        if (isCalendarEnabled && !isHaveCalendarPermission) return false
        return true
    }

    fun toSettings() = SettingsEntity(
        yandexToken = this.yandexToken,
        openWeatherToken = this.openWeatherToken,
        userLat = this.userLat,
        userLng = this.userLng,
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
                userLng = this.userLng,
                userLat = this.userLat,
                isYaWeatherEnabled = this.isWeatherEnabled,
                isCalendarEnabled = this.isCalendarEnabled,
                isOtpEnabled = this.isOtpEnabled,
                isInAppWidgetEnabled = this.isInAppWidgetEnabled
            )
        }
    }
}
