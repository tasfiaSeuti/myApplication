package com.weatherupdate.glare.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import com.weatherupdate.glare.R
import com.weatherupdate.glare.models.MyWeatherData
import com.weatherupdate.glare.models.RealmData
import com.weatherupdate.glare.utilities.OnlyConstants
import io.realm.Realm
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.ProtocolException
import java.net.URL

class SearchActivity : AppCompatActivity() {
    var myWeatherData = MyWeatherData()
    var findCity: String? = null
    var cityName: TextView? = null
    var dateTime: TextView? = null
    var temp: TextView? = null
    var image: ImageView? = null
    var situation: TextView? = null
    var latitude: TextView? = null
    var longitude: TextView? = null
    var pressure: TextView? = null
    var windSpeed: TextView? = null
    var humidity: TextView? = null

    lateinit var lat:String
    lateinit var lon:String

    lateinit var realm: Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        cityName = findViewById(R.id.cityName)
        temp = findViewById(R.id.temperature)
        dateTime = findViewById(R.id.dateTime2)
        image = findViewById(R.id.image)
        situation = findViewById(R.id.situation)
        latitude = findViewById(R.id.latitude3)
        longitude = findViewById(R.id.longitude3)
        humidity = findViewById(R.id.Humidity3)
        pressure = findViewById(R.id.pressure3)
        windSpeed = findViewById(R.id.windSpeed3)

        realm = Realm.getDefaultInstance()

        Mapbox.getInstance(this, getString(R.string.MAPBOX_ACCESS_TOKEN))
        val intent: Intent = PlaceAutocomplete.IntentBuilder()
            .accessToken(OnlyConstants.MAPBOX_ACCESS_TOKEN)
            .placeOptions(placeOptions)
            .build(this)
        startActivityForResult(intent, OnlyConstants.REQUEST_CODE_AUTOCOMPLETE)
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == OnlyConstants.REQUEST_CODE_AUTOCOMPLETE) {
            val feature = PlaceAutocomplete.getPlace(data)
            val data1 = feature.toJson()
            try {
                val jsonObject = JSONObject(data1)
                findCity = jsonObject.getString("text")
                val process = DataFetch()
                process.execute()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }
    fun startMainActivity() {
        realm.executeTransactionAsync({ bgRealm ->
            val realmData = bgRealm.createObject(RealmData::class.java)
            lat= myWeatherData.latitudeOfsearchedPlace.toString()
            lon= myWeatherData.longitudeOfSearchedPlace.toString()
            realmData.searchedLatitude = lat
            realmData.searchedLongitude = lon
        }, {
            val i = Intent(this@SearchActivity, MainActivity::class.java)
            startActivity(i)
        }) {}
    }
    @SuppressLint("StaticFieldLeak")
    inner class DataFetch : AsyncTask<String,Void,String>() {
        override fun doInBackground(vararg params: String): String {
            var inputLine2: String?
            val result = StringBuilder()
            try {
                val url =
                    URL("https://api.openweathermap.org/data/2.5/weather?q=$findCity&units=metric&appid=1ccb72c16c65d0f4afbfbb0c64313fbf")
                val httpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.requestMethod = "GET"
                val inputStream = httpURLConnection.inputStream
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                while (bufferedReader.readLine().also { inputLine2 = it } != null) {
                    result.append(inputLine2)
                }
            } catch (e: ProtocolException) {
                e.printStackTrace()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return result.toString()
        }

        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        override fun onPostExecute(aVoid: String) {
            try {
                val jsonObject = JSONObject(aVoid)
                val object7 = jsonObject.getJSONObject("coord")
                myWeatherData.latitudeOfsearchedPlace = object7.getString("lat")
                val object5 = jsonObject.getJSONObject("coord")
                myWeatherData.longitudeOfSearchedPlace = object5.getString("lon")
                startMainActivity()
            } catch (jsonException: JSONException) {
                jsonException.printStackTrace()
            }
        }
    }

    companion object {
        var placeOptions: PlaceOptions? = null
    }
}