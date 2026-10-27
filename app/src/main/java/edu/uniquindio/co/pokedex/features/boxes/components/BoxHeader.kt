package edu.uniquindio.co.pokedex.features.boxes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.uniquindio.co.pokedex.ui.theme.PokedexGold
import edu.uniquindio.co.pokedex.ui.theme.PokedexGoldDark
import edu.uniquindio.co.pokedex.ui.theme.PokedexInk
import edu.uniquindio.co.pokedex.ui.theme.PressStart2P

/** Encabezado de caja: borde dorado, filo interior oscuro y degradado duro cielo / pasto. */
@Composable
fun BoxHeader(title: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(
                Brush.verticalGradient(
                    0f to Color(0xFF9FD3F5),
                    0.55f to Color(0xFF9FD3F5),
                    0.55f to Color(0xFF6FAE3A),
                    1f to Color(0xFF6FAE3A)
                )
            )
            .border(3.dp, PokedexGold)
            .padding(3.dp)
            .border(2.dp, PokedexGoldDark),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontFamily = PressStart2P,
            fontSize = 10.sp,
            color = PokedexInk
        )
    }
}
