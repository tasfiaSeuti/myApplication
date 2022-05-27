package com.weatherupdate.glare.models

import io.realm.RealmObject

open class RealmData : RealmObject() {
    var searchedLatitude: String? = null
    var searchedLongitude: String? = null
}