package com.alkanyazilim.manzum.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.text.StaticLayout
import android.text.TextPaint
import androidx.core.content.FileProvider
import com.alkanyazilim.manzum.data.Beyit
import java.io.File
import java.io.FileOutputStream

val GRADYAN_HARITASI = mapOf(
    "Aşk" to intArrayOf(Color.parseColor("#E94057"), Color.parseColor("#C4324B")),
    "Hüzün" to intArrayOf(Color.parseColor("#4B5568"), Color.parseColor("#2E3A4E")),
    "Umut" to intArrayOf(Color.parseColor("#E0932A"), Color.parseColor("#C97A1B")),
    "Gurur" to intArrayOf(Color.parseColor("#6B3FA0"), Color.parseColor("#4B2E7A")),
    "Özlem" to intArrayOf(Color.parseColor("#3B6EA5"), Color.parseColor("#274B75")),
    "Yalnızlık" to intArrayOf(Color.parseColor("#1F3A63"), Color.parseColor("#14264A")),
    "Vuslat" to intArrayOf(Color.parseColor("#D6336C"), Color.parseColor("#A61E4D")),
    "Hasret" to intArrayOf(Color.parseColor("#B5651D"), Color.parseColor("#7A4212")),
    "Tefekkür" to intArrayOf(Color.parseColor("#2F4858"), Color.parseColor("#1B2E38")),
    "Neşe" to intArrayOf(Color.parseColor("#F2994A"), Color.parseColor("#EB5C6F")),
    "Gurbet" to intArrayOf(Color.parseColor("#546E7A"), Color.parseColor("#37474F")),
    "Sitem" to intArrayOf(Color.parseColor("#C0392B"), Color.parseColor("#7B241C")),
)
// Not: Bu renkler emoji/temaya göre seçilmiştir. Uygulamanın Compose tarafındaki
// GradVuslat, GradHasret vb. tam renklerle birebir eşleşmesini istersen değerleri
// ui/theme dosyandaki gerçek hex kodlarıyla güncelleyebilirsin.

/**
 * Bir beyit için kategori renginde gradyanlı, "Manzum" imzalı kart görseli üretir.
 * Hem paylaşım kartında hem de bildirim görselinde ortak kullanılır.
 */
fun olusturBeyitKarti(
    context: Context,
    beyit: Beyit,
    genislik: Int = 1080,
    yukseklik: Int = 1350
): Bitmap {
    val bitmap = Bitmap.createBitmap(genislik, yukseklik, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val renkler = GRADYAN_HARITASI[beyit.kategori]
        ?: intArrayOf(Color.parseColor("#0D1123"), Color.parseColor("#1A1F3A"))
    val gradyanPaint = Paint().apply {
        shader = LinearGradient(0f, 0f, 0f, yukseklik.toFloat(), renkler[0], renkler[1], Shader.TileMode.CLAMP)
    }
    canvas.drawRect(RectF(0f, 0f, genislik.toFloat(), yukseklik.toFloat()), gradyanPaint)

    val metinPaint = TextPaint().apply {
        color = Color.WHITE
        textSize = 56f
        isAntiAlias = true
        typeface = Typeface.create(Typeface.SERIF, Typeface.ITALIC)
    }
    val metinLayout = StaticLayout.Builder
        .obtain(beyit.orijinalMetin, 0, beyit.orijinalMetin.length, metinPaint, genislik - 160)
        .setLineSpacing(16f, 1.2f)
        .build()

    canvas.save()
    canvas.translate(80f, (yukseklik - metinLayout.height) / 2f - 60f)
    metinLayout.draw(canvas)
    canvas.restore()

    val altBilgiPaint = TextPaint().apply {
        color = Color.parseColor("#D9D9D9")
        textSize = 36f
        isAntiAlias = true
        typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
    }
    canvas.drawText(beyit.sair, 80f, yukseklik - 160f, altBilgiPaint)

    val markaPaint = TextPaint().apply {
        color = Color.parseColor("#C9A667")
        textSize = 32f
        isAntiAlias = true
        typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
    }
    canvas.drawText("Manzum", 80f, yukseklik - 90f, markaPaint)

    return bitmap
}

fun beyitiPaylas(context: Context, beyit: Beyit) {
    val bitmap = olusturBeyitKarti(context, beyit)

    val klasor = File(context.cacheDir, "paylasim")
    klasor.mkdirs()
    val dosya = File(klasor, "beyit_${beyit.id}.png")
    FileOutputStream(dosya).use { out ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
    }

    val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", dosya)
    val paylasimNiyeti = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(paylasimNiyeti, "Beyiti paylaş"))
}