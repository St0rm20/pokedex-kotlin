package edu.uniquindio.co.pokedex.features.boxes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uniquindio.co.pokedex.core.util.RequestResult
import edu.uniquindio.co.pokedex.core.util.toUserMessage
import edu.uniquindio.co.pokedex.domain.model.BOX_SIZE
import edu.uniquindio.co.pokedex.domain.model.Generation
import edu.uniquindio.co.pokedex.domain.model.PokemonSummary
import edu.uniquindio.co.pokedex.domain.model.PokemonType
import edu.uniquindio.co.pokedex.domain.usecase.GetPokemonDetailUseCase
import edu.uniquindio.co.pokedex.domain.usecase.GetPokemonPageUseCase
import edu.uniquindio.co.pokedex.domain.usecase.ObserveConnectivityUseCase
import edu.uniquindio.co.pokedex.domain.usecase.SearchPokemonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class BoxesViewModel @Inject constructor(
    private val getPokemonPageUseCase: GetPokemonPageUseCase,
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
    private val searchPokemonUseCase: SearchPokemonUseCase,
    private val observeConnectivityUseCase: ObserveConnectivityUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BoxesUiState())
    val uiState: StateFlow<BoxesUiState> = _uiState.asStateFlow()

    private var detailJob: Job? = null
    private var searchJob: Job? = null

    // Lista paginada por la API (sin filtros), para restaurarla al limpiar la búsqueda.
    private var browseItems: List<PokemonSummary> = emptyList()
    private var browseHasNext = true

    // Resultado completo de la búsqueda/filtros; se muestra por tandas en el cliente.
    private var filteredResults: List<PokemonSummary> = emptyList()

    init {
        loadFirstPage()
        observeConnectivity()
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            observeConnectivityUseCase().collect { online ->
                _uiState.update { it.copy(isOnline = online) }
                if (online) retryFailedLoads()
            }
        }
    }

    /** Sin caché persistente: al volver la red se repite lo que haya fallado. */
    private fun retryFailedLoads() {
        val state = _uiState.value
        when {
            state.loadResult is RequestResult.Failure -> retry()
            state.loadMoreFailed -> loadNextPage()
        }
        val selected = state.selected
        if (selected != null && state.selectedDetail == null) selectPokemon(selected)
    }

    fun loadFirstPage() {
        if (_uiState.value.loadResult is RequestResult.Loading) return
        viewModelScope.launch {
            _uiState.update { it.copy(loadResult = RequestResult.Loading) }
            try {
                val page = getPokemonPageUseCase(offset = 0)
                browseItems = page.items
                browseHasNext = page.hasNext
                _uiState.update {
                    // Si el usuario ya empezó a filtrar, esa búsqueda manda sobre la lista.
                    if (it.isFiltering) it else it.copy(
                        pokemon = page.items,
                        hasNext = page.hasNext,
                        loadResult = RequestResult.Success("Caja cargada")
                    )
                }
                if (_uiState.value.selected == null && !_uiState.value.isFiltering) {
                    page.items.firstOrNull()?.let(::selectPokemon)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(loadResult = RequestResult.Failure(e.toUserMessage()))
                }
            }
        }
    }

    fun loadNextPage() {
        val state = _uiState.value
        if (state.isFiltering) {
            showMoreFilteredResults()
            return
        }
        if (state.loadResult !is RequestResult.Success || !state.hasNext || state.isLoadingMore) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true, loadMoreError = null) }
            try {
                val page = getPokemonPageUseCase(offset = _uiState.value.pokemon.size)
                browseItems = browseItems + page.items
                browseHasNext = page.hasNext
                _uiState.update {
                    it.copy(
                        pokemon = it.pokemon + page.items,
                        hasNext = page.hasNext,
                        isLoadingMore = false
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoadingMore = false, loadMoreError = e.toUserMessage()) }
            }
        }
    }

    fun selectPokemon(pokemon: PokemonSummary) {
        detailJob?.cancel()
        _uiState.update { it.copy(selected = pokemon, selectedDetail = null) }
        detailJob = viewModelScope.launch {
            // Si falla, el panel conserva nombre y número sin los tipos.
            val detail = try {
                getPokemonDetailUseCase(pokemon.id)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                null
            }
            _uiState.update { it.copy(selectedDetail = detail) }
        }
    }

    private fun clearSelection() {
        detailJob?.cancel()
        _uiState.update { it.copy(selected = null, selectedDetail = null) }
    }

    fun onQueryChange(query: String) {
        clearSelection()
        _uiState.update { it.copy(query = query) }
        applyFilters(debounce = true)
    }

    fun onTypeSelected(type: PokemonType?) {
        clearSelection()
        _uiState.update { it.copy(selectedType = type) }
        applyFilters(debounce = false)
    }

    fun onGenerationSelected(generation: Generation?) {
        clearSelection()
        _uiState.update { it.copy(selectedGeneration = generation) }
        applyFilters(debounce = false)
    }

    fun retry() {
        if (_uiState.value.isFiltering) applyFilters(debounce = false) else loadFirstPage()
    }

    private fun applyFilters(debounce: Boolean) {
        searchJob?.cancel()
        val state = _uiState.value
        if (!state.isFiltering) {
            restoreBrowse()
            return
        }
        searchJob = viewModelScope.launch {
            if (debounce) delay(SEARCH_DEBOUNCE_MS)
            _uiState.update {
                it.copy(
                    pokemon = emptyList(),
                    hasNext = false,
                    isLoadingMore = false,
                    loadMoreError = null,
                    loadResult = RequestResult.Loading
                )
            }
            try {
                filteredResults = searchPokemonUseCase(
                    query = state.query,
                    type = state.selectedType,
                    generation = state.selectedGeneration
                )
                _uiState.update {
                    it.copy(
                        pokemon = filteredResults.take(BOX_SIZE),
                        hasNext = filteredResults.size > BOX_SIZE,
                        loadResult = RequestResult.Success("Búsqueda completada")
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(loadResult = RequestResult.Failure(e.toUserMessage()))
                }
            }
        }
    }

    private fun restoreBrowse() {
        if (browseItems.isEmpty()) {
            _uiState.update { it.copy(loadResult = null, pokemon = emptyList()) }
            loadFirstPage()
            return
        }
        _uiState.update {
            it.copy(
                pokemon = browseItems,
                hasNext = browseHasNext,
                isLoadingMore = false,
                loadMoreError = null,
                loadResult = RequestResult.Success("Caja cargada")
            )
        }
    }

    private fun showMoreFilteredResults() {
        _uiState.update {
            if (it.loadResult !is RequestResult.Success || !it.hasNext) return@update it
            val visible = minOf(it.pokemon.size + BOX_SIZE, filteredResults.size)
            it.copy(pokemon = filteredResults.take(visible), hasNext = visible < filteredResults.size)
        }
    }
}

private const val SEARCH_DEBOUNCE_MS = 300L
