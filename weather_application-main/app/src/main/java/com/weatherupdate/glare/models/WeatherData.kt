package com.weatherupdate.glare.models

class WeatherData {
    var weather: List<Weather>? = null
    var timezone = 0f
    var id = 0f
    var name: String? = null
    var main: Main? = null
    var coord: Coord? = null
    var wind: Wind? = null
    var sys: Sys? = null
}