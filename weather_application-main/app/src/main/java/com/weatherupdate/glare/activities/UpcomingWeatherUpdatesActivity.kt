package com.weatherupdate.glare.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.weatherupdate.glare.R
import com.weatherupdate.glare.adapters.WeatherDataAdaptor
import com.weatherupdate.glare.models.UpcomingWeatherData
import com.weatherupdate.glare.utilities.OnlyConstants
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class UpcomingWeatherUpdatesActivity : AppCompatActivity() {
    private var listView: ListView? = null
    lateinit var temp2:String
    var date: String? = null
    var weatherSituation: String? = null
    var weatherImageCode: String? = null
    var weatherImage: String? = null
    var dateInExpectedFormat: String? = null
    var latitude2: String? = null
    var longitude2: String? = null
    var weatherList: ArrayList<UpcomingWeatherData>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcomingforecast)
        weatherList = ArrayList()
        listView = findViewById(R.id.listView)
        val extras = intent.extras
        if (extras != null) {
            latitude2 = extras.getString("Latitude")
            longitude2 = extras.getString("Longitude")
        }
        val getData = GetData(listView)
        getData.execute()
    }

    inner class GetData(var listview: ListView?) : AsyncTask<String?, String?, String>() {
        override fun doInBackground(vararg params: String?): String {
            var current: String? = ""
            val result2 = StringBuilder()
            val lat2 = latitude2.toString()
            val long2 = longitude2.toString()
            try {
                val urlConnection: HttpURLConnection
                try {
                    val url2 =
                        URL("https://api.openweathermap.org/data/2.5/onecall?lat=$lat2&lon=$long2&exclude=hourly,minutely&units=metric&appid=1ccb72c16c65d0f4afbfbb0c64313fbf")
                    urlConnection = url2.openConnection() as HttpURLConnection
                    val `in` = urlConnection.inputStream
                    val br = BufferedReader(InputStreamReader(`in`))
                    while (br.readLine().also { current = it } != null) {
                        result2.append(current)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result2.toString()
        }

        @SuppressLint("SimpleDateFormat")
        override fun onPostExecute(s: String) {
            try {
                val jo = JSONObject(s)
                val ja = jo.getJSONArray("daily")
                for (i in 1 until ja.length()) {
                    val daily = ja.getJSONObject(i)
                    val ja2 = daily.getJSONArray("weather")
                    val m = ja2.getJSONObject(0)
                    weatherSituation = m.getString("main")

                    val temperature = daily.getJSONObject("temp")
                    temp2 = temperature.getString("day")
                    date = daily.getString("dt").toString()
                    val dateParse = date!!.toLong()*100
                    val dateFormat = Date(dateParse)
                    dateInExpectedFormat = SimpleDateFormat("dd/MM/yyyy ").format(dateFormat)
                    val ja3 = daily.getJSONArray("weather")
                    val icon = ja3.getJSONObject(0)
                    weatherImageCode = icon.getString("icon")
                    weatherImage = OnlyConstants.IMG_URL2 + weatherImageCode + ".png"
                    val weatherdata = UpcomingWeatherData(
                        temp2, weatherSituation!!,
                        weatherImage!!,
                        dateInExpectedFormat!!
                    )
                    weatherList!!.add(weatherdata)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            val adapter =
                weatherList?.let { WeatherDataAdaptor(this@UpcomingWeatherUpdatesActivity, it) }
            listview!!.adapter = adapter
        }
    }
}