package edu.uniquindio.co.pokedex.features.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import edu.uniquindio.co.pokedex.core.component.TypeChip
import edu.uniquindio.co.pokedex.core.util.RequestResult
import edu.uniquindio.co.pokedex.domain.model.PokemonDetail
import edu.uniquindio.co.pokedex.domain.model.PokemonSpecies
import edu.uniquindio.co.pokedex.ui.theme.PressStart2P
import edu.uniquindio.co.pokedex.ui.theme.stripedBackground
import edu.uniquindio.co.pokedex.ui.theme.PokedexInk
import edu.uniquindio.co.pokedex.ui.theme.PokedexRed
import edu.uniquindio.co.pokedex.ui.theme.PokedexRedDark
import java.util.Locale

private val Ink = Color(0xFF4A3B22)
private val Paper = Color(0xFFFBF6E2)
private const val MAX_STAT = 150

@Composable
fun DetailScreen(
    id: Int,
    onNavigateBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(id) { viewModel.load(id) }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PokedexRed)
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .background(Color(0xFF8E2A1F), RoundedCornerShape(6.dp))
                        .border(3.dp, Color.White, RoundedCornerShape(6.dp))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver a las cajas",
                        tint = Color.White
                    )
                }
                Text(
                    text = "POKÉDEX",
                    fontFamily = PressStart2P,
                    fontSize = 14.sp,
                    color = Color.White,
                    style = LocalTextStyle.current.copy(
                        shadow = Shadow(PokedexRedDark, Offset(4f, 4f), 0f)
                    ),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "No. ${id.toString().padStart(3, '0')}",
                    fontFamily = PressStart2P,
                    fontSize = 9.sp,
                    color = Color.White,
                    modifier = Modifier
                        .background(PokedexRedDark)
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .stripedBackground()
                .padding(padding)
        ) {
            when (val result = state.loadResult) {
                null, RequestResult.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))

                is RequestResult.Failure -> Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = result.errorMessage, color = PokedexInk)
                    Button(onClick = { viewModel.load(id) }) { Text("REINTENTAR") }
                }

                is RequestResult.Success -> state.detail?.let { detail ->
                    DetailContent(detail = detail, species = state.species)
                }
            }
        }
    }
}

@Composable
private fun DetailContent(detail: PokemonDetail, species: PokemonSpecies?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Card {
            Row(
                modifier = Modifier.padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.White)
                        .border(3.dp, Ink, RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = detail.artworkUrl,
                        contentDescription = "Ilustración oficial de ${detail.name}",
                        modifier = Modifier.size(124.dp)
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "No. ${detail.id.toString().padStart(3, '0')}",
                        fontFamily = PressStart2P,
                        fontSize = 9.sp,
                        color = Ink
                    )
                    Text(
                        text = detail.name.uppercase(),
                        fontFamily = PressStart2P,
                        fontSize = 13.sp,
                        color = PokedexInk
                    )
                    species?.genus?.let {
                        Text(
                            text = it.uppercase(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Ink
                        )
                    }
                    MeasureRow("ALT.", String.format(Locale("es"), "%.1f m", detail.heightMeters))
                    MeasureRow("PESO", String.format(Locale("es"), "%.1f kg", detail.weightKg))
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        detail.types.forEach { TypeChip(it) }
                    }
                }
            }
            species?.description?.let {
                Text(
                    text = it,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = PokedexInk,
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(6.dp))
                        .border(3.dp, Ink, RoundedCornerShape(6.dp))
                        .padding(10.dp)
                )
            }
        }

        Section("HABILIDADES") {
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                detail.abilities.forEach {
                    Text(
                        text = if (it.isHidden) "${it.name} (oculta)" else it.name,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PokedexInk,
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(4.dp))
                            .border(2.dp, Ink, RoundedCornerShape(4.dp))
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                    )
                }
            }
        }

        Section("ESTADÍSTICAS BASE") {
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                detail.stats.forEach { stat ->
                    Row(
                        modifier = Modifier.height(28.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stat.name,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = PokedexInk,
                            modifier = Modifier.width(88.dp)
                        )
                        StatBar(value = stat.value, modifier = Modifier.weight(1f))
                        Text(
                            text = stat.value.toString(),
                            fontFamily = PressStart2P,
                            fontSize = 9.sp,
                            color = PokedexInk,
                            modifier = Modifier.width(28.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatBar(value: Int, modifier: Modifier = Modifier) {
    val color = when {
        value >= 80 -> Color(0xFF58C858)
        value >= 50 -> Color(0xFFF8D030)
        else -> Color(0xFFE04848)
    }
    Box(
        modifier = modifier
            .height(14.dp)
            .background(Color(0xFF3A3A3A), RoundedCornerShape(7.dp))
            .padding(2.dp)
            .background(Color(0xFFF4F0E0), RoundedCornerShape(4.dp))
            .clip(RoundedCornerShape(4.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth((value.toFloat() / MAX_STAT).coerceIn(0f, 1f))
                .background(color)
        )
    }
}

@Composable
private fun MeasureRow(label: String, value: String) {
    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(text = label, fontFamily = PressStart2P, fontSize = 8.sp, color = Ink)
        Box(modifier = Modifier.weight(1f))
        Text(text = value, fontFamily = PressStart2P, fontSize = 9.sp, color = PokedexInk)
    }
}

@Composable
private fun Card(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Paper, RoundedCornerShape(10.dp))
            .border(3.dp, Ink, RoundedCornerShape(10.dp)),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) { content() }
}

@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Paper)
            .border(3.dp, Ink, RoundedCornerShape(10.dp))
    ) {
        Text(
            text = title,
            fontFamily = PressStart2P,
            fontSize = 9.sp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .background(PokedexRed)
                .padding(horizontal = 12.dp, vertical = 9.dp)
        )
        content()
    }
}
