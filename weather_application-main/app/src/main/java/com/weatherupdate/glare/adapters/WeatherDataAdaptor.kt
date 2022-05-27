package com.weatherupdate.glare.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.weatherupdate.glare.R
import com.weatherupdate.glare.models.UpcomingWeatherData

class WeatherDataAdaptor(context: Context, weatherData: ArrayList<UpcomingWeatherData>) :
    ArrayAdapter<UpcomingWeatherData?>(context, R.layout.row_layout, weatherData as List<UpcomingWeatherData?>) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val convertedView = inflater.inflate(R.layout.row_layout, null, true)
        val temp2 = convertedView.findViewById<View>(R.id.textView20) as TextView
        val date2 = convertedView.findViewById<View>(R.id.textView21) as TextView
        val rain = convertedView.findViewById<View>(R.id.rain) as TextView
        val imageView = convertedView.findViewById<View>(R.id.imageView2) as ImageView
        val temperature = getItem(position)
        temp2.text=temperature?.tempOfUpcomingWeather.toString()
        rain.text = temperature?.upcomingWeatherSituation
        date2.text = temperature?.dateInExpectedFormat
        Picasso.get().load(temperature?.upcomingWeatherImage).placeholder(R.drawable.hello).into(imageView)
        return convertedView
    }
}