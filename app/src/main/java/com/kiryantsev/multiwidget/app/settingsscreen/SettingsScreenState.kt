package com.kiryantsev.multiwidget.app.settingsscreen

import com.kiryantsev.multiwidget.core.settings.SettingsEntity

data class SettingsScreenState(
    val yandexToken: String,
    val userLat: Double,
    val userLng: Double,
    val isWeatherEnabled: Boolean,
    val isCalendarEnabled: Boolean,
    val isOtpEnabled: Boolean,
    val isInAppWidgetEnabled: Boolean,
    val isHaveCalendarPermission: Boolean = false,
    val locationFetchingInProgress: Boolean? = false,
) {

    fun isSaveButtonEnabled(): Boolean {
        if (isWeatherEnabled && yandexToken.isEmpty() && (userLat == .0 || userLng == .0)) return false
        if (isCalendarEnabled && !isHaveCalendarPermission) return false
        return true
    }

    fun toSettings() = SettingsEntity(
        yandexToken = this.yandexToken,
        userLat = this.userLat,
        userLng = this.userLng,
        isWeatherEnabled = this.isWeatherEnabled,
        isCalendarEnabled = this.isCalendarEnabled,
        isOtpEnabled = this.isOtpEnabled,
        isInAppWidgetEnabled = isInAppWidgetEnabled
    )

    companion object {
        fun getFromSettings() = SettingsEntity.load().run {
            SettingsScreenState(
                yandexToken = this.yandexToken,
                userLng = this.userLng,
                userLat = this.userLat,
                isWeatherEnabled = this.isWeatherEnabled,
                isCalendarEnabled = this.isCalendarEnabled,
                isOtpEnabled = this.isOtpEnabled,
                isInAppWidgetEnabled = this.isInAppWidgetEnabled
            )
        }
    }
}
