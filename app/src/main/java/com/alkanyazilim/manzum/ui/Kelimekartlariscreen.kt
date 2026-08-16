package com.alkanyazilim.manzum.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alkanyazilim.manzum.data.KelimeKarti
import com.alkanyazilim.manzum.ui.theme.ManzumBg
import com.alkanyazilim.manzum.ui.theme.ManzumCardTextLight
import com.alkanyazilim.manzum.ui.theme.ManzumGold
import com.alkanyazilim.manzum.ui.theme.ManzumTextMuted

@Composable
fun KelimeKartlariScreen(
    guncelKart: KelimeKarti?,
    bilinenSayisi: Int,
    toplamSayisi: Int,
    kategoriIlerlemesi: List<ManzumViewModel.KategoriIlerlemesi>,
    onBiliyorum: () -> Unit,
    onTekrarGoster: () -> Unit
) {
    var detayAcik by remember { mutableStateOf(false) }
    // Her yeni kart geldiğinde kartın ön yüze dönmesi için kelime.id'yi anahtar yapıyoruz
    var cevrilmis by remember(guncelKart?.id) { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .background(ManzumBg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(24.dp))
        Text(
            "Kelime Kartlarım",
            color = ManzumCardTextLight,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp
        )
        Spacer(Modifier.height(20.dp))

        // İlerleme çubuğu
        val ilerlemeOrani = if (toplamSayisi > 0) bilinenSayisi / toplamSayisi.toFloat() else 0f
        Text(
            "$bilinenSayisi / $toplamSayisi kelime biliniyor",
            color = ManzumTextMuted,
            fontSize = 13.sp
        )
        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { ilerlemeOrani },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = ManzumGold,
            trackColor = ManzumTextMuted.copy(alpha = 0.25f)
        )
        Spacer(Modifier.height(16.dp))

        // Kategoriye göre detay (açılır/kapanır)
        Row(
            Modifier
                .fillMaxWidth()
                .clickable { detayAcik = !detayAcik },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Kategoriye Göre Detay", color = ManzumCardTextLight, fontSize = 15.sp)
            Text(if (detayAcik) "▲" else "▼", color = ManzumTextMuted, fontSize = 13.sp)
        }
        if (detayAcik) {
            Spacer(Modifier.height(10.dp))
            kategoriIlerlemesi.forEach { kat ->
                Column(Modifier.padding(vertical = 6.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(kat.kategori, color = ManzumCardTextLight, fontSize = 13.sp)
                        val yuzde = if (kat.toplam > 0) (kat.bilinen * 100 / kat.toplam) else 0
                        Text(
                            "${kat.bilinen} / ${kat.toplam}  (%$yuzde)",
                            color = ManzumGold,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    val katOrani = if (kat.toplam > 0) kat.bilinen / kat.toplam.toFloat() else 0f
                    LinearProgressIndicator(
                        progress = { katOrani },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = ManzumGold,
                        trackColor = ManzumTextMuted.copy(alpha = 0.2f)
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        if (guncelKart == null) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF1A1F3A)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Tüm kelimeleri biliyorsun! 🎉",
                    color = ManzumCardTextLight,
                    fontFamily = FontFamily.Serif,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(24.dp)
                )
            }
        } else {
            CevrilebilirKelimeKarti(
                kart = guncelKart,
                cevrilmis = cevrilmis,
                onTikla = { cevrilmis = !cevrilmis }
            )

            Spacer(Modifier.height(12.dp))
            Text(
                if (cevrilmis) "Ön yüze dönmek için dokun" else "Kartı çevirmek için dokun",
                color = ManzumTextMuted,
                fontStyle = FontStyle.Italic,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onTekrarGoster,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Tekrar Göster", color = ManzumCardTextLight)
                }
                Button(
                    onClick = onBiliyorum,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = ManzumGold)
                ) {
                    Text("Biliyorum ✓", color = Color.Black)
                }
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun CevrilebilirKelimeKarti(
    kart: KelimeKarti,
    cevrilmis: Boolean,
    onTikla: () -> Unit
) {
    val donusAcisi by animateFloatAsState(
        targetValue = if (cevrilmis) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "kart_donusu"
    )

    Box(
        Modifier
            .fillMaxWidth()
            .height(360.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF1A1F3A))
            .clickable { onTikla() }
            .graphicsLayer {
                rotationY = donusAcisi
                cameraDistance = 12f * density
            },
        contentAlignment = Alignment.Center
    ) {
        if (donusAcisi <= 90f) {
            // Ön yüz — sadece kelime
            Text(
                kart.kelime,
                color = ManzumCardTextLight,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(24.dp)
            )
        } else {
            // Arka yüz — anlam ve örnek dize (dönüşün aynalanmasını düzeltmek için tekrar 180° çevrilir)
            Column(
                Modifier
                    .padding(24.dp)
                    .graphicsLayer { rotationY = 180f },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    kart.kelime,
                    color = ManzumGold,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    kart.anlam,
                    color = ManzumCardTextLight,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
                if (kart.ornekDize != "—") {
                    Spacer(Modifier.height(20.dp))
                    Text(
                        "\"${kart.ornekDize}\"",
                        color = ManzumTextMuted,
                        fontFamily = FontFamily.Serif,
                        fontStyle = FontStyle.Italic,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}