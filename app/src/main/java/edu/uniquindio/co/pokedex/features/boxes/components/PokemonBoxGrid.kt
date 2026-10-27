package edu.uniquindio.co.pokedex.features.boxes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import edu.uniquindio.co.pokedex.domain.model.BOX_SIZE
import edu.uniquindio.co.pokedex.domain.model.PokemonSummary
import edu.uniquindio.co.pokedex.ui.theme.PokedexGold
import edu.uniquindio.co.pokedex.ui.theme.PokedexGoldDark
import edu.uniquindio.co.pokedex.ui.theme.PokedexNavy
import edu.uniquindio.co.pokedex.ui.theme.PressStart2P
import kotlinx.coroutines.delay

private const val LOAD_MORE_THRESHOLD = 6

/** Cuadrícula de 3 columnas con encabezados de caja, scroll infinito y barra de scroll decorativa. */
@Composable
fun PokemonBoxGrid(
    pokemon: List<PokemonSummary>,
    selectedId: Int?,
    isFiltered: Boolean,
    resetKey: Any,
    isLoadingMore: Boolean,
    loadMoreError: String?,
    padding: PaddingValues,
    onLoadMore: () -> Unit,
    onSelect: (PokemonSummary) -> Unit,
    modifier: Modifier = Modifier
) {
    val boxes = if (isFiltered) listOf(pokemon) else pokemon.chunked(BOX_SIZE)
    // Al cambiar la búsqueda o los filtros la lista vuelve al inicio.
    val gridState = remember(resetKey) { LazyGridState() }

    val nearEnd by remember(gridState) {
        derivedStateOf {
            val info = gridState.layoutInfo
            val last = info.visibleItemsInfo.lastOrNull()?.index ?: -1
            info.totalItemsCount > 0 && last >= info.totalItemsCount - LOAD_MORE_THRESHOLD
        }
    }
    LaunchedEffect(nearEnd, pokemon.size, loadMoreError) {
        if (nearEnd && loadMoreError == null) onLoadMore()
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            contentPadding = padding,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            boxes.forEachIndexed { index, box ->
                item(key = "header-$index", span = { GridItemSpan(maxLineSpan) }) {
                    // La cuadrícula separa 4 dp; el encabezado necesita 6 dp a cada lado.
                    BoxHeader(
                        title = if (isFiltered) "RESULTADOS" else "CAJA ${index + 1}",
                        modifier = Modifier.padding(top = if (index == 0) 0.dp else 2.dp, bottom = 2.dp)
                    )
                }
                items(box, key = { it.id }) {
                    PokemonCell(pokemon = it, selected = it.id == selectedId, onClick = { onSelect(it) })
                }
            }
            if (isLoadingMore) {
                item(key = "loading-more", span = { GridItemSpan(maxLineSpan) }) { LoadingMoreFooter() }
            }
            if (loadMoreError != null) {
                item(key = "load-more-error", span = { GridItemSpan(maxLineSpan) }) {
                    LoadMoreErrorFooter(message = loadMoreError, onRetry = onLoadMore)
                }
            }
        }
        ScrollIndicator(
            state = gridState,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .fillMaxHeight()
                .padding(top = 4.dp, bottom = 4.dp, end = 3.dp)
        )
    }
}

@Composable
private fun PokemonCell(
    pokemon: PokemonSummary,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // La selección parpadea por pasos entre amarillo y blanco (ciclo de 0,9 s).
    var bright by remember { mutableStateOf(true) }
    LaunchedEffect(selected) {
        bright = true
        while (selected) {
            delay(450)
            bright = !bright
        }
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(if (selected) Color.White.copy(alpha = 0.28f) else Color.Transparent)
            .then(
                if (selected) {
                    Modifier.border(3.dp, if (bright) Color(0xFFFFE14D) else Color.White)
                } else Modifier
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = pokemon.spriteUrl,
            contentDescription = pokemon.name,
            contentScale = ContentScale.Fit,
            filterQuality = FilterQuality.None,
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        )
        Text(
            text = "#${pokemon.id.toString().padStart(3, '0')}",
            fontFamily = PressStart2P,
            fontSize = 8.sp,
            lineHeight = 8.sp,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(4.dp)
                .background(PokedexNavy)
                .padding(horizontal = 4.dp, vertical = 3.dp)
        )
    }
}

/** Barra de scroll solo visual: su posición y tamaño salen del estado de la cuadrícula. */
@Composable
private fun ScrollIndicator(state: LazyGridState, modifier: Modifier = Modifier) {
    val thumb by remember(state) {
        derivedStateOf {
            val info = state.layoutInfo
            val total = info.totalItemsCount
            val visible = info.visibleItemsInfo.size
            if (total == 0 || visible == 0) {
                1f to 0f
            } else {
                val fraction = (visible.toFloat() / total).coerceIn(0.08f, 1f)
                val scrollable = (total - visible).coerceAtLeast(1)
                fraction to (state.firstVisibleItemIndex.toFloat() / scrollable).coerceIn(0f, 1f)
            }
        }
    }
    val (fraction, progress) = thumb
    androidx.compose.foundation.layout.BoxWithConstraints(
        modifier = modifier
            .width(6.dp)
            .background(Color(0xFF2B2B2B).copy(alpha = 0.28f))
    ) {
        val thumbHeight: Dp = maxHeight * fraction
        Box(
            modifier = Modifier
                .offset(y = (maxHeight - thumbHeight) * progress)
                .fillMaxWidth()
                .height(thumbHeight)
                .background(PokedexGold)
                .border(1.dp, PokedexGoldDark)
        )
    }
}

/** Pie "Cargando caja siguiente...": tres cuadros que se encienden en secuencia. */
@Composable
private fun LoadingMoreFooter(modifier: Modifier = Modifier) {
    var tick by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(300)
            tick = (tick + 1) % 4
        }
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(Color(0xFFE6F4FD))
            .border(3.dp, Color(0xFF2E6DB4)),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            repeat(3) { dot ->
                // Cada cuadro se enciende en la segunda mitad de su ciclo, desfasado 0,3 s.
                val on = Math.floorMod(tick - dot, 4) >= 2
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(Color(0xFF1D3F7A).copy(alpha = if (on) 1f else 0.25f))
                )
            }
        }
        Text(
            text = "Cargando caja siguiente...",
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1D2B4A)
        )
    }
}

@Composable
private fun LoadMoreErrorFooter(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(Color(0xFFE6F4FD))
            .border(3.dp, Color(0xFF2E6DB4)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = message,
            fontSize = 11.sp,
            color = Color(0xFFA32A1E),
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )
        TextButton(onClick = onRetry) { Text("REINTENTAR", fontSize = 11.sp) }
    }
}
