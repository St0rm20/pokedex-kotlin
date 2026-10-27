package edu.uniquindio.co.pokedex.domain.usecase

import edu.uniquindio.co.pokedex.domain.model.BOX_SIZE
import edu.uniquindio.co.pokedex.domain.model.PokemonPage
import edu.uniquindio.co.pokedex.domain.repository.PokemonRepository
import jakarta.inject.Inject

class GetPokemonPageUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(offset: Int = 0, limit: Int = BOX_SIZE): PokemonPage =
        repository.getPokemonPage(limit = limit, offset = offset)
}
