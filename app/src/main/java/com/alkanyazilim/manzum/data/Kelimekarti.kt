package com.alkanyazilim.manzum.data

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.IOException

@Serializable
data class KelimeKarti(
    val id: Int,
    val kelime: String,
    val anlam: String,
    val ornekDize: String,
    val kategori: String
)

/**
 * assets/kelimeler.json dosyasından tüm kelime kartlarını okur.
 * Dosya bulunamazsa veya parse hatası olursa boş liste döner.
 */
fun loadKelimeKartlari(context: Context): List<KelimeKarti> {
    return try {
        val jsonString = context.assets.open("kelimeler.json")
            .bufferedReader()
            .use { it.readText() }
        Json { ignoreUnknownKeys = true }.decodeFromString(jsonString)
    } catch (e: IOException) {
        emptyList()
    } catch (e: Exception) {
        emptyList()
    }
}