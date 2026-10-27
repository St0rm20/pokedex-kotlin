package edu.uniquindio.co.pokedex.features.boxes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.uniquindio.co.pokedex.core.component.WifiOffIcon
import edu.uniquindio.co.pokedex.ui.theme.PokedexGoldDark
import edu.uniquindio.co.pokedex.ui.theme.PokedexInk
import edu.uniquindio.co.pokedex.ui.theme.PressStart2P
import edu.uniquindio.co.pokedex.ui.theme.outerRing

private val ErrorRed = Color(0xFFA32A1E)

/** Tarjeta centrada del estado de error de red, con botón REINTENTAR. */
@Composable
fun NetworkErrorCard(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth().padding(16.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .outerRing(Color(0xFF1D3F7A), 3.dp)
                .background(Color(0xFFE6F4FD))
                .border(3.dp, Color(0xFF2E6DB4))
                .padding(horizontal = 16.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            WifiOffIcon(size = 44.dp, color = ErrorRed, strokeWidth = 2.5f)
            Text(
                text = "ERROR DE RED",
                fontFamily = PressStart2P,
                fontSize = 11.sp,
                lineHeight = 16.sp,
                color = ErrorRed,
                textAlign = TextAlign.Center
            )
            Text(
                text = message,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PokedexInk,
                textAlign = TextAlign.Center
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
                    .background(Color(0xFFC9A227))
                    .border(3.dp, PokedexGoldDark)
                    .clickable(onClick = onRetry),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "REINTENTAR",
                    fontFamily = PressStart2P,
                    fontSize = 10.sp,
                    lineHeight = 15.sp,
                    color = PokedexInk
                )
            }
        }
    }
}
