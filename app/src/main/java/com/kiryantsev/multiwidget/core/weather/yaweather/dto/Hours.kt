package com.kiryantsev.multiwidget.core.weather.yaweather.dto

import com.google.gson.annotations.SerializedName

data class Hours(

    @SerializedName("hour") val hour : Int,
    @SerializedName("hour_ts") val hourTs : Int,
    @SerializedName("temp") val temp : Int,
    @SerializedName("feels_like") val feelsLike : Int,
    @SerializedName("icon") val icon : String,
    @SerializedName("condition") val condition : String,
    @SerializedName("wind_speed") val windSpeed : Double,
    @SerializedName("wind_gust") val windGust : Double,
    @SerializedName("wind_dir") val windDir : String,
    @SerializedName("pressure_mm") val pressureMm : Int,
    @SerializedName("pressure_pa") val pressurePa : Int,
    @SerializedName("humidity") val humidity : Int,
    @SerializedName("uv_index") val uvIndex : Int,
    @SerializedName("soil_temp") val soilTemp : Int,
    @SerializedName("soil_moisture") val soilMoisture : Double,
    @SerializedName("prec_mm") val precMm : Double,
    @SerializedName("prec_period") val precPeriod : Int,
    @SerializedName("prec_prob") val precProb : Int

)
