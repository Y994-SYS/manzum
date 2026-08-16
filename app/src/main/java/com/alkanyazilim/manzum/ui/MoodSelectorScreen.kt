package com.alkanyazilim.manzum.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alkanyazilim.manzum.data.KATEGORI_GORUNUMLERI
import com.alkanyazilim.manzum.ui.theme.ManzumBg
import com.alkanyazilim.manzum.ui.theme.ManzumCardTextLight
import com.alkanyazilim.manzum.ui.theme.ManzumGold
import com.alkanyazilim.manzum.ui.theme.ManzumTextMuted
import androidx.compose.foundation.border
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.MenuBook
@Composable
fun MoodSelectorScreen(
    onKategoriSecildi: (String) -> Unit,
    onFavorilerTikla: () -> Unit,
    onSairlerTikla: () -> Unit,
    onKelimeKartlariTikla: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(ManzumBg)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(28.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(
                    "Manzum",
                    color = ManzumCardTextLight,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
                Text(
                    "Divan ustalarından günümüze",
                    color = ManzumTextMuted,
                    fontSize = 13.sp
                )
            }
            Row {
                IconButton(onClick = onKelimeKartlariTikla) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Filled.School,
                        contentDescription = "Kelime Kartlarım",
                        tint = ManzumGold
                    )
                }
                IconButton(onClick = onSairlerTikla) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Filled.MenuBook,
                        contentDescription = "Şairler",
                        tint = ManzumGold
                    )
                }
                IconButton(onClick = onFavorilerTikla) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Filled.Favorite,
                        contentDescription = "Favorilerim",
                        tint = ManzumGold
                    )
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        Text(
            "Bugün ruhun hangi hâlde?",
            color = ManzumCardTextLight,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        )
        Spacer(Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)

        ) {
            items(KATEGORI_GORUNUMLERI) { kg ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Brush.verticalGradient(kg.gradyan))
                        .clickable { onKategoriSecildi(kg.ad) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(kg.emoji, fontSize = 28.sp)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            kg.gosterimEtiketi,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF241B2E))
                .border(1.dp, ManzumGold.copy(alpha = 0.0f), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Box(
                Modifier
                    .width(3.dp)
                    .fillMaxHeight()
                    .background(ManzumGold)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                "💡 Her gün bir beyit, her duygu bir hikaye. Şairlerin diliyle kendini bul.",
                color = ManzumTextMuted,
                fontStyle = FontStyle.Italic,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
        Spacer(Modifier.height(24.dp))
    }
}