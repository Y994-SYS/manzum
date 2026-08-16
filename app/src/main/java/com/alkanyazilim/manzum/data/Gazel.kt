package com.alkanyazilim.manzum.data

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.IOException

@Serializable
data class Gazel(
    val sair: String,
    val baslik: String,
    val beyitler: List<String>, // her öğe bir beyit (iki dize, \n ile ayrılmış)
    val kaynak: String
)

/**
 * assets/gazeller.json dosyasından tüm gazelleri okur.
 * Dosya bulunamazsa veya parse hatası olursa boş liste döner
 * (uygulama çökmez, PoetDetailScreen "henüz gazel eklenmedi" mesajını gösterir).
 */
fun loadGazeller(context: Context): List<Gazel> {
    return try {
        val jsonString = context.assets.open("gazeller.json")
            .bufferedReader()
            .use { it.readText() }
        Json { ignoreUnknownKeys = true }.decodeFromString(jsonString)
    } catch (e: IOException) {
        emptyList()
    } catch (e: Exception) {
        emptyList()
    }
}