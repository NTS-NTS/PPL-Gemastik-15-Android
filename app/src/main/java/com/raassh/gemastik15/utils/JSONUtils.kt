package com.raassh.gemastik15.utils

import android.content.Context
import com.raassh.gemastik15.R
import org.json.JSONArray

fun Context.getCities(): Array<String> {
    val cities = mutableListOf<String>()

    // source: https://github.com/mtegarsantosa/json-nama-daerah-indonesia
    val regionsJson = resources.openRawResource(R.raw.regions)
    val regionsString = regionsJson.bufferedReader().use { it.readText() }

    val regionsArray = JSONArray(regionsString)
    for (i in 0 until regionsArray.length()) {
        val regions = regionsArray.getJSONObject(i)
        val citiesArray = regions.getJSONArray("kota")

        for (j in 0 until citiesArray.length()) {
            val city = citiesArray.getString(j)
            cities.add(city)
        }
    }

    return cities.toTypedArray()
}