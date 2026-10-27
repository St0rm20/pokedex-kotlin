package edu.uniquindio.co.pokedex.core.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.uniquindio.co.pokedex.domain.model.PokemonType
import edu.uniquindio.co.pokedex.ui.theme.PokedexInk

@Composable
fun TypeChip(type: PokemonType, modifier: Modifier = Modifier) {
    Text(
        text = type.label.uppercase(),
        fontSize = 10.sp,
        lineHeight = 14.sp,
        fontWeight = FontWeight.ExtraBold,
        color = if (type.lightText()) Color.White else PokedexInk,
        modifier = modifier
            .background(type.color())
            .border(2.dp, PokedexInk)
            .padding(horizontal = 6.dp, vertical = 1.dp)
    )
}

private fun PokemonType.color(): Color = when (this) {
    PokemonType.NORMAL -> Color(0xFFA8A878)
    PokemonType.FIRE -> Color(0xFFF08030)
    PokemonType.WATER -> Color(0xFF6890F0)
    PokemonType.ELECTRIC -> Color(0xFFF8D030)
    PokemonType.GRASS -> Color(0xFF78C850)
    PokemonType.ICE -> Color(0xFF98D8D8)
    PokemonType.FIGHTING -> Color(0xFFC03028)
    PokemonType.POISON -> Color(0xFFA040A0)
    PokemonType.GROUND -> Color(0xFFE0C068)
    PokemonType.FLYING -> Color(0xFFA890F0)
    PokemonType.PSYCHIC -> Color(0xFFF85888)
    PokemonType.BUG -> Color(0xFFA8B820)
    PokemonType.ROCK -> Color(0xFFB8A038)
    PokemonType.GHOST -> Color(0xFF705898)
    PokemonType.DRAGON -> Color(0xFF7038F8)
    PokemonType.DARK -> Color(0xFF705848)
    PokemonType.STEEL -> Color(0xFFB8B8D0)
    PokemonType.FAIRY -> Color(0xFFEE99AC)
}

private fun PokemonType.lightText(): Boolean = when (this) {
    PokemonType.POISON, PokemonType.FIGHTING, PokemonType.GHOST,
    PokemonType.DRAGON, PokemonType.DARK -> true
    else -> false
}
