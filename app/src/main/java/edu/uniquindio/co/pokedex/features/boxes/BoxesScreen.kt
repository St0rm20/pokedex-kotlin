package edu.uniquindio.co.pokedex.features.boxes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.uniquindio.co.pokedex.core.util.RequestResult
import edu.uniquindio.co.pokedex.features.boxes.components.BoxesTopBar
import edu.uniquindio.co.pokedex.features.boxes.components.FilterChips
import edu.uniquindio.co.pokedex.features.boxes.components.InfoPanel
import edu.uniquindio.co.pokedex.features.boxes.components.NetworkErrorCard
import edu.uniquindio.co.pokedex.features.boxes.components.OfflineBanner
import edu.uniquindio.co.pokedex.features.boxes.components.PokemonBoxGrid
import edu.uniquindio.co.pokedex.features.boxes.components.SearchBar
import edu.uniquindio.co.pokedex.ui.theme.PokedexBackground
import edu.uniquindio.co.pokedex.ui.theme.PokedexGold
import edu.uniquindio.co.pokedex.ui.theme.PokedexGoldDark
import edu.uniquindio.co.pokedex.ui.theme.PokedexInk
import edu.uniquindio.co.pokedex.ui.theme.fieldBackground

@Composable
fun BoxesScreen(
    onNavigateToDetail: (Int) -> Unit,
    viewModel: BoxesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { BoxesTopBar(isOnline = state.isOnline) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(PokedexBackground)
        ) {
            SearchBar(query = state.query, onQueryChange = viewModel::onQueryChange)
            FilterChips(
                selectedType = state.selectedType,
                selectedGeneration = state.selectedGeneration,
                onTypeSelected = viewModel::onTypeSelected,
                onGenerationSelected = viewModel::onGenerationSelected
            )
            if (!state.isOnline) OfflineBanner()
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(start = 6.dp, end = 6.dp, bottom = 6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                InfoPanel(
                    selected = state.selected,
                    detail = state.selectedDetail,
                    onViewDetail = { state.selected?.let { onNavigateToDetail(it.id) } }
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(PokedexGoldDark)
                        .border(4.dp, PokedexGold)
                        .padding(2.dp)
                        .fieldBackground()
                ) {
                    when (val result = state.loadResult) {
                        null, RequestResult.Loading -> if (state.pokemon.isEmpty()) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }

                        is RequestResult.Failure -> if (state.pokemon.isEmpty()) {
                            NetworkErrorCard(
                                message = result.errorMessage,
                                onRetry = viewModel::retry,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }

                        is RequestResult.Success -> if (state.pokemon.isEmpty() && state.isFiltering) {
                            Text(
                                text = "Sin resultados",
                                fontWeight = FontWeight.ExtraBold,
                                color = PokedexInk,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                    if (state.pokemon.isNotEmpty()) {
                        PokemonBoxGrid(
                            pokemon = state.pokemon,
                            selectedId = state.selected?.id,
                            isFiltered = state.isFiltering,
                            resetKey = Triple(state.query, state.selectedType, state.selectedGeneration),
                            isLoadingMore = state.isLoadingMore,
                            loadMoreError = state.loadMoreError,
                            padding = PaddingValues(start = 4.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
                            onLoadMore = viewModel::loadNextPage,
                            onSelect = viewModel::selectPokemon
                        )
                    }
                }
            }
        }
    }
}
