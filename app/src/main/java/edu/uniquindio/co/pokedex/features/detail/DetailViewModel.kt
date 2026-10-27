package edu.uniquindio.co.pokedex.features.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uniquindio.co.pokedex.core.util.RequestResult
import edu.uniquindio.co.pokedex.core.util.toUserMessage
import edu.uniquindio.co.pokedex.domain.usecase.GetPokemonDetailUseCase
import edu.uniquindio.co.pokedex.domain.usecase.GetPokemonSpeciesUseCase
import edu.uniquindio.co.pokedex.domain.usecase.ObserveConnectivityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
    private val getPokemonSpeciesUseCase: GetPokemonSpeciesUseCase,
    private val observeConnectivityUseCase: ObserveConnectivityUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private var currentId: Int? = null

    init {
        // Al volver la red se repite la carga si la última falló.
        viewModelScope.launch {
            observeConnectivityUseCase().collect { online ->
                val id = currentId
                if (online && id != null && _uiState.value.loadResult is RequestResult.Failure) load(id)
            }
        }
    }

    fun load(id: Int) {
        currentId = id
        if (_uiState.value.loadResult is RequestResult.Loading) return
        viewModelScope.launch {
            _uiState.update { it.copy(loadResult = RequestResult.Loading) }
            try {
                val (detail, species) = coroutineScope {
                    val detail = async { getPokemonDetailUseCase(id) }
                    // La descripción es complementaria: si falla, se muestra el resto.
                    val species = async {
                        try {
                            getPokemonSpeciesUseCase(id)
                        } catch (e: CancellationException) {
                            throw e
                        } catch (e: Exception) {
                            null
                        }
                    }
                    detail.await() to species.await()
                }
                _uiState.update {
                    it.copy(
                        detail = detail,
                        species = species,
                        loadResult = if (detail != null) {
                            RequestResult.Success("Pokémon cargado")
                        } else {
                            RequestResult.Failure("No se encontró el Pokémon")
                        }
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
}
