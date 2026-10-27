package edu.uniquindio.co.pokedex.domain.usecase

import edu.uniquindio.co.pokedex.domain.model.PokemonSpecies
import edu.uniquindio.co.pokedex.domain.repository.PokemonRepository
import jakarta.inject.Inject

class GetPokemonSpeciesUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(id: Int): PokemonSpecies? = repository.getPokemonSpecies(id)
}
