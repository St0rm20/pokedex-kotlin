package edu.uniquindio.co.pokedex.domain.usecase

import edu.uniquindio.co.pokedex.domain.model.PokemonDetail
import edu.uniquindio.co.pokedex.domain.repository.PokemonRepository
import jakarta.inject.Inject

class GetPokemonDetailUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(id: Int): PokemonDetail? = repository.getPokemonDetail(id)
}
