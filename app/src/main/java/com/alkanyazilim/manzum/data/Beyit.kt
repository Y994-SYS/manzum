package com.alkanyazilim.manzum.data

import androidx.compose.ui.graphics.Color
import com.alkanyazilim.manzum.ui.theme.*

data class Beyit(
    val id: Int,
    val sair: String,
    val kategori: String,
    val orijinalMetin: String,
    val sadelestirme: String,
    val yorum: String,
    val sairBilgisi: String,
    val kaynak: String
)

val KATEGORILER = listOf("Aşk", "Hüzün", "Umut", "Gurur", "Özlem", "Yalnızlık", "Vuslat", "Hasret", "Tefekkür", "Neşe", "Gurbet", "Sitem")
data class KategoriGorunum(
    val ad: String,
    val gosterimEtiketi: String,
    val emoji: String,
    val gradyan: List<Color>
)

val KATEGORI_GORUNUMLERI = listOf(
    KategoriGorunum("Aşk", "Aşk & Hicran", "💔", GradAsk),
    KategoriGorunum("Hüzün", "Hüzün & Melal", "🌧️", GradHuzun),
    KategoriGorunum("Umut", "Umut & Tevekkül", "☀️", GradUmut),
    KategoriGorunum("Gurur", "Gurur & İrade", "🦅", GradGurur),
    KategoriGorunum("Özlem", "Özlem & Vuslat", "🌙", GradOzlem),
    KategoriGorunum("Yalnızlık", "Yalnızlık & Tefekkür", "🕯️", GradYalnizlik),
    KategoriGorunum("Vuslat", "Vuslat & Kavuşma", "🌹", GradVuslat),
    KategoriGorunum("Hasret", "Hasret & Firak", "🍂", GradHasret),
    KategoriGorunum("Tefekkür", "Tefekkür & Hikmet", "📿", GradTefekkur),
    KategoriGorunum("Neşe", "Neşe & Şevk", "🎉", GradNese),
    KategoriGorunum("Gurbet", "Gurbet & Sıla", "🏔️", GradGurbet),
    KategoriGorunum("Sitem", "Sitem & Serzeniş", "⚡", GradSitem),
)