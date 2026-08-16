package com.alkanyazilim.manzum.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alkanyazilim.manzum.data.Beyit
import com.alkanyazilim.manzum.ui.theme.ManzumBg
import com.alkanyazilim.manzum.ui.theme.ManzumCardTextLight
import com.alkanyazilim.manzum.ui.theme.ManzumGold
import com.alkanyazilim.manzum.ui.theme.ManzumTextMuted
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.filled.Share

@Composable
fun PoemDetailScreen(
    beyit: Beyit?,
    favoriMi: Boolean = false,
    onFavoriTikla: () -> Unit = {},
    onPaylasTikla: () -> Unit = {}
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(ManzumBg)
    ) {
        if (beyit == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Bu kategoride henüz beyit yok.", color = ManzumTextMuted)
            }
            return@Box
        }
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    beyit.sair,
                    color = ManzumGold,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
                Row {
                    IconButton(onClick = { onPaylasTikla() }) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Paylaş",
                            tint = ManzumTextMuted
                        )
                    }
                    IconButton(onClick = onFavoriTikla) {
                        Icon(
                            imageVector = if (favoriMi) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorilere ekle",
                            tint = if (favoriMi) Color(0xFFE94057) else ManzumTextMuted
                        )
                    }
                }
            }
            Spacer(Modifier.height(4.dp))
                Spacer(Modifier.height(4.dp))
                Text(beyit.kategori, color = ManzumTextMuted, fontSize = 13.sp)
                Spacer(Modifier.height(24.dp))

                Text(
                    beyit.orijinalMetin,
                    color = ManzumCardTextLight,
                    fontFamily = FontFamily.Serif,
                    fontStyle = FontStyle.Italic,
                    fontSize = 22.sp,
                    lineHeight = 32.sp
                )
                Spacer(Modifier.height(28.dp))

                Text(
                    "Günümüz Türkçesiyle",
                    color = ManzumGold,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    beyit.sadelestirme,
                    color = ManzumCardTextLight,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
                Spacer(Modifier.height(28.dp))

                Text(
                    "Neden bugün de geçerli?",
                    color = ManzumGold,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(beyit.yorum, color = ManzumTextMuted, fontSize = 14.sp, lineHeight = 21.sp)
                Spacer(Modifier.height(28.dp))

                Text(beyit.sairBilgisi, color = ManzumTextMuted, fontSize = 12.sp)
            }
        }
    }
