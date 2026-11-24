package com.alkanyazilim.manzum.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
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

    // --- Arama ---
    var aramaMetni by remember { mutableStateOf("") }
    val filtrelenmisSairler = remember(sairler, aramaMetni) {
        if (aramaMetni.isBlank()) {
            sairler.keys.toList()
        } else {
            sairler.keys.filter { it.contains(aramaMetni, ignoreCase = true) }
        }
    }

    Column(Modifier.fillMaxSize().background(ManzumBg).padding(20.dp)) {
        Text(
            "Şairler",
            color = ManzumCardTextLight,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Spacer(Modifier.height(16.dp))

        // --- Arama kutusu ---
        OutlinedTextField(
            value = aramaMetni,
            onValueChange = { aramaMetni = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Şair ara...", color = ManzumTextMuted) },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = ManzumTextMuted) },
            trailingIcon = {
                if (aramaMetni.isNotEmpty()) {
                    IconButton(onClick = { aramaMetni = "" }) {
                        Icon(Icons.Filled.Close, contentDescription = "Aramayı temizle", tint = ManzumTextMuted)
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = ManzumCardTextLight,
                unfocusedTextColor = ManzumCardTextLight,
                focusedBorderColor = ManzumGold,
                unfocusedBorderColor = ManzumTextMuted.copy(alpha = 0.4f),
                cursorColor = ManzumGold
            )
        )
        Spacer(Modifier.height(16.dp))

        if (filtrelenmisSairler.isEmpty()) {
            Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                Text(
                    "\"$aramaMetni\" için şair bulunamadı",
                    color = ManzumTextMuted,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(filtrelenmisSairler) { sair ->
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
}