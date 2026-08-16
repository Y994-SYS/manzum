package com.alkanyazilim.manzum.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alkanyazilim.manzum.data.Beyit
import com.alkanyazilim.manzum.ui.theme.ManzumBg
import com.alkanyazilim.manzum.ui.theme.ManzumCardTextLight
import com.alkanyazilim.manzum.ui.theme.ManzumGold
import com.alkanyazilim.manzum.ui.theme.ManzumTextMuted

@Composable
fun FavoritesScreen(
    favoriler: List<Beyit>,
    onBeyitTikla: (Beyit) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(ManzumBg)
            .padding(20.dp)
    ) {
        Text(
            "Favorilerim",
            color = ManzumCardTextLight,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Spacer(Modifier.height(16.dp))

        if (favoriler.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "Henüz favori beyitin yok.\nBir beyitteki kalbe dokunarak buraya ekleyebilirsin.",
                    color = ManzumTextMuted,
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(favoriler) { beyit ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(androidx.compose.ui.graphics.Color(0xFF1A1F3A))
                            .clickable { onBeyitTikla(beyit) }
                            .padding(16.dp)
                    ) {
                        Text(beyit.sair, color = ManzumGold, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            beyit.orijinalMetin.lineSequence().first(),
                            color = ManzumCardTextLight,
                            fontFamily = FontFamily.Serif,
                            fontStyle = FontStyle.Italic,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}