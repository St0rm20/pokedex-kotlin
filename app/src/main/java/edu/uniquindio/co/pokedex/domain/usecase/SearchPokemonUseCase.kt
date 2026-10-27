package edu.uniquindio.co.pokedex.domain.usecase

import edu.uniquindio.co.pokedex.domain.model.Generation
import edu.uniquindio.co.pokedex.domain.model.PokemonSummary
import edu.uniquindio.co.pokedex.domain.model.PokemonType
import edu.uniquindio.co.pokedex.domain.repository.PokemonRepository
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * Combina los tres campos de filtro: nombre (índice en memoria), tipo y generación.
 * Con varios campos activos devuelve la intersección, ordenada por id.
 */
class SearchPokemonUseCase @Inject constructor(
    private val repository: PokemonRepository,
    private val getPokemonByType: GetPokemonByTypeUseCase,
    private val getPokemonByGeneration: GetPokemonByGenerationUseCase
) {
    suspend operator fun invoke(
        query: String,
        type: PokemonType?,
        generation: Generation?
    ): List<PokemonSummary> = coroutineScope {
        val name = query.trim()
        val byName = if (name.isNotEmpty()) {
            async { repository.getPokemonIndex().filter { it.name.contains(name, ignoreCase = true) } }
        } else null
        val byType = type?.let { async { getPokemonByType(it) } }
        val byGeneration = generation?.let { async { getPokemonByGeneration(it) } }

        val lists = listOfNotNull(byName, byType, byGeneration).map { it.await() }
        val ids = lists.drop(1).map { list -> list.mapTo(HashSet()) { it.id } }
        lists.firstOrNull().orEmpty()
            .filter { pokemon -> ids.all { pokemon.id in it } }
            .sortedBy { it.id }
    }
}
