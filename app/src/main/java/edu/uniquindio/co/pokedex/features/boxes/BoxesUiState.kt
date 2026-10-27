package edu.uniquindio.co.pokedex.features.boxes

import edu.uniquindio.co.pokedex.core.util.RequestResult
import edu.uniquindio.co.pokedex.domain.model.Generation
import edu.uniquindio.co.pokedex.domain.model.PokemonDetail
import edu.uniquindio.co.pokedex.domain.model.PokemonSummary
import edu.uniquindio.co.pokedex.domain.model.PokemonType

data class BoxesUiState(
    val pokemon: List<PokemonSummary> = emptyList(),
    val selected: PokemonSummary? = null,
    val selectedDetail: PokemonDetail? = null,
    val loadResult: RequestResult? = null,
    val hasNext: Boolean = true,
    val isLoadingMore: Boolean = false,
    val loadMoreError: String? = null,
    val isOnline: Boolean = true,
    val query: String = "",
    val selectedType: PokemonType? = null,
    val selectedGeneration: Generation? = null
) {
    val loadMoreFailed: Boolean
        get() = loadMoreError != null

    val isFiltering: Boolean
        get() = query.isNotBlank() || selectedType != null || selectedGeneration != null
}
