package edu.uniquindio.co.pokedex.domain.usecase

import edu.uniquindio.co.pokedex.domain.model.PokemonSummary
import edu.uniquindio.co.pokedex.domain.model.PokemonType
import edu.uniquindio.co.pokedex.domain.repository.PokemonRepository
import jakarta.inject.Inject

class GetPokemonByTypeUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(type: PokemonType): List<PokemonSummary> =
        repository.getPokemonByType(type)
}
