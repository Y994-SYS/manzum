package com.alkanyazilim.manzum.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alkanyazilim.manzum.data.Beyit
import com.alkanyazilim.manzum.data.Gazel
import com.alkanyazilim.manzum.ui.theme.ManzumBg
import com.alkanyazilim.manzum.ui.theme.ManzumCardTextLight
import com.alkanyazilim.manzum.ui.theme.ManzumGold
import com.alkanyazilim.manzum.ui.theme.ManzumTextMuted

@Composable
fun PoetDetailScreen(
    sair: String,
    sairinBeyitleri: List<Beyit>,
    tumGazeller: List<Gazel>
) {
    val gazel = tumGazeller.find { it.sair == sair }

    Column(
        Modifier
            .fillMaxSize()
            .background(ManzumBg)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(sair, color = ManzumCardTextLight, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 26.sp)
        Spacer(Modifier.height(20.dp))

        if (gazel != null) {
            Text(gazel.baslik, color = ManzumGold, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            Spacer(Modifier.height(16.dp))
            gazel.beyitler.forEach { beyit ->
                Text(
                    beyit,
                    color = ManzumCardTextLight,
                    fontFamily = FontFamily.Serif,
                    fontStyle = FontStyle.Italic,
                    fontSize = 18.sp,
                    lineHeight = 28.sp
                )
                Spacer(Modifier.height(18.dp))
            }
            Spacer(Modifier.height(8.dp))
            Text(gazel.kaynak, color = ManzumTextMuted, fontSize = 11.sp)
        } else {
            Text(
                "Bu şairin henüz tam gazeli eklenmedi, kısa beyitlerini aşağıda bulabilirsin:",
                color = ManzumTextMuted,
                fontStyle = FontStyle.Italic,
                fontSize = 13.sp
            )
            Spacer(Modifier.height(16.dp))
            sairinBeyitleri.forEach { beyit ->
                Text(
                    beyit.orijinalMetin,
                    color = ManzumCardTextLight,
                    fontFamily = FontFamily.Serif,
                    fontStyle = FontStyle.Italic,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}