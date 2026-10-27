package edu.uniquindio.co.pokedex.features.boxes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.uniquindio.co.pokedex.ui.theme.PokedexRed
import edu.uniquindio.co.pokedex.ui.theme.PokedexRedDark
import edu.uniquindio.co.pokedex.ui.theme.PressStart2P

/** Barra superior de 52 dp: título, indicador ONLINE/OFFLINE, línea clara interior y borde oscuro. */
@Composable
fun BoxesTopBar(isOnline: Boolean, modifier: Modifier = Modifier) {
    val shadowOffset = with(LocalDensity.current) { 2.dp.toPx() }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(PokedexRed)
            .statusBarsPadding()
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(48.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "POKÉDEX",
                    fontFamily = PressStart2P,
                    fontSize = 16.sp,
                    color = Color.White,
                    style = LocalTextStyle.current.copy(
                        shadow = Shadow(PokedexRedDark, Offset(shadowOffset, shadowOffset), 0f)
                    )
                )
                ConnectionIndicator(isOnline = isOnline)
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(Color(0xFFE8705F))
            )
        }
        Box(modifier = Modifier.fillMaxWidth().height(4.dp).background(PokedexRedDark))
    }
}

@Composable
fun ConnectionIndicator(isOnline: Boolean, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(PokedexRedDark, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(if (isOnline) Color(0xFF5BD16A) else Color(0xFFE24B3B))
                .border(2.dp, Color.White)
        )
        Text(
            text = if (isOnline) "ONLINE" else "OFFLINE",
            fontFamily = PressStart2P,
            fontSize = 9.sp,
            color = Color.White
        )
    }
}
