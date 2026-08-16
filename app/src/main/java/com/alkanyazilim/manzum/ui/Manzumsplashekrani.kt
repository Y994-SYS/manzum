package com.alkanyazilim.manzum.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.alkanyazilim.manzum.ui.theme.ManzumBg
import com.alkanyazilim.manzum.ui.theme.ManzumGold

/**
 * Native Android SplashScreen API (ikon+arka plan) kapandıktan hemen sonra
 * kısa bir süre gösterilen, "Manzum" yazılı markalı geçiş ekranı.
 * Native API doğrudan metin desteklemediği için bu şekilde tamamlanıyor.
 */
@Composable
fun ManzumSplashEkrani() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ManzumBg),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Manzum",
            color = ManzumGold,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 42.sp
        )
    }
}