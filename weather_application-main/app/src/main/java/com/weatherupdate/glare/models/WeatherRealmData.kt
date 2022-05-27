package com.weatherupdate.glare.models

import io.realm.RealmObject

open class WeatherRealmData : RealmObject() {
    var timezone = 0
    var country: String? = null
    var city: String? = null
    var temperature: String? = null
    var img: String? = null
    var humidity: String? = null
    var sunrise: String? = null
    var sunset: String? = null
    var pressure: String? = null
    var latitude: String? = null
    var longitude: String? = null
    var wind_speed: String? = null
}