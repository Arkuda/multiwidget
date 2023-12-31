package com.kiryantsev.multiwidget.core.weather.yaweather.dto

import com.google.gson.annotations.SerializedName

data class Night_short(
    @SerializedName("temp") val temp : Int,
    @SerializedName("feels_like") val feels_like : Int,
    @SerializedName("icon") val icon : String,
    @SerializedName("condition") val condition : String,
    @SerializedName("wind_speed") val wind_speed : Double,
    @SerializedName("wind_gust") val wind_gust : Double,
    @SerializedName("wind_dir") val wind_dir : String,
    @SerializedName("pressure_mm") val pressure_mm : Int,
    @SerializedName("pressure_pa") val pressure_pa : Int,
    @SerializedName("humidity") val humidity : Int,
    @SerializedName("uv_index") val uv_index : Int,
    @SerializedName("soil_temp") val soil_temp : Int,
    @SerializedName("soil_moisture") val soil_moisture : Double,
    @SerializedName("prec_mm") val prec_mm : Double,
    @SerializedName("prec_prob") val prec_prob : Int
)