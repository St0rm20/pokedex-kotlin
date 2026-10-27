package edu.uniquindio.co.pokedex.features.boxes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import edu.uniquindio.co.pokedex.core.component.TypeChip
import edu.uniquindio.co.pokedex.data.mapper.spriteUrl
import edu.uniquindio.co.pokedex.domain.model.PokemonDetail
import edu.uniquindio.co.pokedex.domain.model.PokemonSummary
import edu.uniquindio.co.pokedex.ui.theme.PokedexInk
import edu.uniquindio.co.pokedex.ui.theme.PokedexNavy
import edu.uniquindio.co.pokedex.ui.theme.PokedexRedDark
import edu.uniquindio.co.pokedex.ui.theme.PressStart2P
import edu.uniquindio.co.pokedex.ui.theme.checkerBackground
import edu.uniquindio.co.pokedex.ui.theme.outerRing

/** Silueta que se muestra cuando no hay ningún Pokémon seleccionado (Pikachu, como en el mockup). */
private const val SILHOUETTE_ID = 25

/** Franja horizontal INFO PKMN: sprite, datos y botón "VER DETALLE" a la derecha. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InfoPanel(
    selected: PokemonSummary?,
    detail: PokemonDetail?,
    onViewDetail: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(Color(0xFFE6F4FD))
            .border(3.dp, PokedexNavy)
            .padding(3.dp)
            .border(2.dp, Color.White)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(84.dp)
                .checkerBackground()
                .border(3.dp, Color(0xFF2E6DB4)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = spriteUrl(selected?.id ?: SILHOUETTE_ID),
                contentDescription = selected?.name,
                filterQuality = FilterQuality.None,
                colorFilter = if (selected == null) ColorFilter.tint(Color.Black, BlendMode.SrcIn) else null,
                modifier = Modifier
                    .size(78.dp)
                    .alpha(if (selected == null) 0.3f else 1f)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.Start
        ) {
            if (selected == null) {
                Text(
                    text = "Selecciona un Pokémon",
                    fontSize = 13.sp,
                    lineHeight = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PokedexInk
                )
            } else {
                Text(
                    text = "INFO PKMN",
                    fontFamily = PressStart2P,
                    fontSize = 8.sp,
                    color = Color.White,
                    modifier = Modifier
                        .background(Color(0xFF55638A))
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                )
                Text(
                    text = selected.name.uppercase(),
                    fontFamily = PressStart2P,
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    color = PokedexInk
                )
                Text(
                    text = "No. ${selected.id.toString().padStart(3, '0')}",
                    fontFamily = PressStart2P,
                    fontSize = 9.sp,
                    color = Color(0xFF3B4558)
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    detail?.types?.forEach { TypeChip(it) }
                }
            }
        }
        ViewDetailButton(enabled = selected != null, onClick = onViewDetail)
    }
}

@Composable
private fun ViewDetailButton(enabled: Boolean, onClick: () -> Unit) {
    val contentColor = if (enabled) Color.White else Color(0xFF3B4558)
    val shape = RoundedCornerShape(6.dp)
    Column(
        modifier = Modifier
            .width(64.dp)
            .fillMaxHeight()
            .then(if (enabled) Modifier.outerRing(PokedexRedDark, 2.dp, 6.dp) else Modifier)
            .background(if (enabled) Color(0xFF8E2A1F) else Color(0xFFB8C0CF), if (enabled) shape else androidx.compose.ui.graphics.RectangleShape)
            .border(3.dp, if (enabled) Color.White else Color(0xFF7C879B), if (enabled) shape else androidx.compose.ui.graphics.RectangleShape)
            .clickable(enabled = enabled, onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = "VER DETALLE",
            fontFamily = PressStart2P,
            fontSize = 8.sp,
            lineHeight = 12.sp,
            textAlign = TextAlign.Center,
            color = contentColor
        )
    }
}
