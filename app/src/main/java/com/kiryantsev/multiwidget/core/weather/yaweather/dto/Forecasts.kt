package com.kiryantsev.multiwidget.core.weather.yaweather.dto

import com.google.gson.annotations.SerializedName

data class Forecasts(

    @SerializedName("date") val date : String,
    @SerializedName("date_ts") val dateTs : Double,
    @SerializedName("week") val week : Double,
    @SerializedName("sunrise") val sunrise : String,
    @SerializedName("sunset") val sunset : String,
    @SerializedName("rise_begin") val riseBegin : String,
    @SerializedName("set_end") val setEnd : String,
    @SerializedName("moon_code") val moonCode : Double,
    @SerializedName("moon_text") val moonText : String,
    @SerializedName("parts") val parts : Parts,
    @SerializedName("hours") val hours : List<Hours>

)

