package edu.uniquindio.co.pokedex

import edu.uniquindio.co.pokedex.data.mapper.toDomain
import edu.uniquindio.co.pokedex.data.remote.dto.GenerationResponseDto
import edu.uniquindio.co.pokedex.data.remote.dto.TypeResponseDto
import edu.uniquindio.co.pokedex.domain.model.Generation
import edu.uniquindio.co.pokedex.domain.model.PokemonDetail
import edu.uniquindio.co.pokedex.domain.model.PokemonPage
import edu.uniquindio.co.pokedex.domain.model.PokemonSpecies
import edu.uniquindio.co.pokedex.domain.model.PokemonSummary
import edu.uniquindio.co.pokedex.domain.model.PokemonType
import edu.uniquindio.co.pokedex.domain.repository.PokemonRepository
import edu.uniquindio.co.pokedex.domain.usecase.GetPokemonByGenerationUseCase
import edu.uniquindio.co.pokedex.domain.usecase.GetPokemonByTypeUseCase
import edu.uniquindio.co.pokedex.domain.usecase.SearchPokemonUseCase
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchFiltersTest {

    private val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }

    private val typeJson = """{"pokemon":[
        {"slot":1,"pokemon":{"name":"charmander","url":"https://pokeapi.co/api/v2/pokemon/4/"}},
        {"slot":1,"pokemon":{"name":"skeledirge","url":"https://pokeapi.co/api/v2/pokemon/911/"}},
        {"slot":1,"pokemon":{"name":"fuecoco","url":"https://pokeapi.co/api/v2/pokemon/909/"}},
        {"slot":1,"pokemon":{"name":"charizard-mega-x","url":"https://pokeapi.co/api/v2/pokemon/10034/"}}]}"""

    private val generationJson = """{"pokemon_species":[
        {"name":"fuecoco","url":"https://pokeapi.co/api/v2/pokemon-species/909/"},
        {"name":"sprigatito","url":"https://pokeapi.co/api/v2/pokemon-species/906/"},
        {"name":"skeledirge","url":"https://pokeapi.co/api/v2/pokemon-species/911/"}]}"""

    private class FakeRepository(
        val byType: List<PokemonSummary>,
        val byGeneration: List<PokemonSummary>
    ) : PokemonRepository {
        override suspend fun getPokemonPage(limit: Int, offset: Int): PokemonPage = error("no")
        override suspend fun getPokemonDetail(id: Int): PokemonDetail? = error("no")
        override suspend fun getPokemonSpecies(id: Int): PokemonSpecies? = error("no")
        override suspend fun getPokemonIndex(): List<PokemonSummary> = byType + byGeneration
        override suspend fun getPokemonByType(type: PokemonType) = byType
        override suspend fun getPokemonByGeneration(generation: Generation) = byGeneration
    }

    private fun useCase(): SearchPokemonUseCase {
        val repo = FakeRepository(
            byType = json.decodeFromString<TypeResponseDto>(typeJson).toDomain(),
            byGeneration = json.decodeFromString<GenerationResponseDto>(generationJson).toDomain()
        )
        return SearchPokemonUseCase(repo, GetPokemonByTypeUseCase(repo), GetPokemonByGenerationUseCase(repo))
    }

    @Test
    fun typeAndGeneration_returnIntersectionSortedById() = runBlocking {
        val result = useCase()("", PokemonType.FIRE, Generation.IX)
        assertEquals(listOf(909, 911), result.map { it.id })
    }

    @Test
    fun onlyType_ignoresGeneration() = runBlocking {
        val result = useCase()("", PokemonType.FIRE, null)
        assertEquals(listOf(4, 909, 911, 10034), result.map { it.id })
    }

    @Test
    fun onlyGeneration_ignoresType() = runBlocking {
        val result = useCase()("", null, Generation.IX)
        assertEquals(listOf(906, 909, 911), result.map { it.id })
    }
}
