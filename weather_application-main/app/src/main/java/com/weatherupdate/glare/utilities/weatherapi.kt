package com.weatherupdate.glare.utilities

import com.weatherupdate.glare.models.WeatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface weatherapi {
    @GET("weather")
    fun getweather(
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
        @Query("appid") app_id: String?
    ): Call<WeatherData?>?
}