package edu.uniquindio.co.pokedex.features.detail

import edu.uniquindio.co.pokedex.core.util.RequestResult
import edu.uniquindio.co.pokedex.domain.model.PokemonDetail
import edu.uniquindio.co.pokedex.domain.model.PokemonSpecies

data class DetailUiState(
    val detail: PokemonDetail? = null,
    val species: PokemonSpecies? = null,
    val loadResult: RequestResult? = null
)
