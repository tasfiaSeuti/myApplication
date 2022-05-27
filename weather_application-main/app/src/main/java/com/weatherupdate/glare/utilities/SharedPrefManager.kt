package com.weatherupdate.glare.utilities

import android.annotation.SuppressLint
import android.content.Context

class SharedPrefManager(context: Context?) {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null
        fun setPauseTime(key: String?, value: String?) {
            val sharedPreferences = context?.getSharedPreferences("MySharedPref", 0)
            val editor = sharedPreferences?.edit()
            editor?.putString(key, value)
            editor?.apply()
        }

        fun getPauseTime(key: String?, value: String?): String? {
            val prefs = context?.getSharedPreferences("MySharedPref", 0)
            return prefs?.getString(key, value)
        }
    }

    init {
        Companion.context = context
    }
}