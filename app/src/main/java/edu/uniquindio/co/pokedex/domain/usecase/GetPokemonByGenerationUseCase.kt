package edu.uniquindio.co.pokedex.domain.usecase

import edu.uniquindio.co.pokedex.domain.model.Generation
import edu.uniquindio.co.pokedex.domain.model.PokemonSummary
import edu.uniquindio.co.pokedex.domain.repository.PokemonRepository
import jakarta.inject.Inject

class GetPokemonByGenerationUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(generation: Generation): List<PokemonSummary> =
        repository.getPokemonByGeneration(generation)
}
