package com.diasandharits.weatherapp.data

import com.google.gson.annotations.SerializedName

/**
 * Coord is retrieved from android location service
 * so there is no point to use coord in the response but api needs it
 */

data class ForecastResponse(
	@field:SerializedName("cnt")
	val cnt: Int? = null,

	@field:SerializedName("cod")
	val cod: String? = null,

	@field:SerializedName("message")
	val message: Int? = null,

	@field:SerializedName("list")
	val list: List<ListItem?>? = null,
)

data class WeatherItem(

	@field:SerializedName("icon")
	val icon: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("main")
	val main: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,
)

data class Rain(

	@field:SerializedName("3h")
	val jsonMember3h: Double? = null,
)

data class Sys(

	@field:SerializedName("pod")
	val pod: String? = null,
)

data class Wind(

	@field:SerializedName("deg")
	val deg: Int? = null,

	@field:SerializedName("speed")
	val speed: Double? = null,

	@field:SerializedName("gust")
	val gust: Double? = null,
)

data class ListItem(

    @field:SerializedName("dt_txt")
    val dtTxt: String? = null,

    @field:SerializedName("weather")
    val weather: List<WeatherItem?>? = null,

    @field:SerializedName("main")
    val main: Main? = null,
)

data class Main(

	@field:SerializedName("temp_min")
	val tempMin: Double? = null,

	@field:SerializedName("temp_max")
	val tempMax: Double? = null,
)
