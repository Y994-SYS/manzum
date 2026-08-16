package com.alkanyazilim.manzum.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class BeyitRepository(private val context: Context) {
    fun tumBeyitler(): List<Beyit> {
        val json = context.assets.open("beyitler.json")
            .bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<Beyit>>() {}.type
        return Gson().fromJson(json, type)
    }
}