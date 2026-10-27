package edu.uniquindio.co.pokedex.features.boxes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.uniquindio.co.pokedex.core.component.WifiOffIcon
import edu.uniquindio.co.pokedex.ui.theme.PokedexGoldDark
import edu.uniquindio.co.pokedex.ui.theme.PokedexInk

/** Franja amarilla de 32 dp que aparece solo sin conexión. */
@Composable
fun OfflineBanner(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp)
            .height(32.dp)
            .background(Color(0xFFF6D56B))
            .drawBehind {
                val stroke = 3.dp.toPx()
                drawRect(PokedexGoldDark, size = androidx.compose.ui.geometry.Size(size.width, stroke))
                drawRect(
                    PokedexGoldDark,
                    topLeft = androidx.compose.ui.geometry.Offset(0f, size.height - stroke),
                    size = androidx.compose.ui.geometry.Size(size.width, stroke)
                )
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        WifiOffIcon(size = 18.dp, color = PokedexInk, strokeWidth = 3f)
        Text(
            text = "Sin conexión",
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            color = PokedexInk
        )
    }
}
