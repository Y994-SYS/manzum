package com.alkanyazilim.manzum.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alkanyazilim.manzum.data.Beyit
import com.alkanyazilim.manzum.ui.theme.ManzumBg
import com.alkanyazilim.manzum.ui.theme.ManzumCardTextLight
import com.alkanyazilim.manzum.ui.theme.ManzumGold
import com.alkanyazilim.manzum.ui.theme.ManzumTextMuted

@Composable
fun PoetsScreen(
    tumBeyitler: List<Beyit>,
    onSairTikla: (String) -> Unit
) {
    val sairler = tumBeyitler.groupBy { it.sair }.toSortedMap()

    Column(Modifier.fillMaxSize().background(ManzumBg).padding(20.dp)) {
        Text(
            "Şairler",
            color = ManzumCardTextLight,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Spacer(Modifier.height(16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(sairler.keys.toList()) { sair ->
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF1A1F3A))
                        .clickable { onSairTikla(sair) }
                        .padding(16.dp)
                ) {
                    Text(sair, color = ManzumGold, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "${sairler[sair]?.size ?: 0} beyit",
                        color = ManzumTextMuted,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}