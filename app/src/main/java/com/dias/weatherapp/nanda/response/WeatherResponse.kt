package com.nanda.weatherapp.nanda.response

import com.google.gson.annotations.SerializedName

/**
 * Coord is retrieved from android location service
 */

data class WeatherResponse(
    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("weather")
    val weather: List<WeatherItem>? = null,

    @field:SerializedName("main")
    val main: Main? = null,

    )