package edu.uniquindio.co.pokedex.data.repository

import edu.uniquindio.co.pokedex.data.mapper.toDomain
import edu.uniquindio.co.pokedex.data.remote.api.PokeApiService
import edu.uniquindio.co.pokedex.domain.model.Generation
import edu.uniquindio.co.pokedex.domain.model.PokemonDetail
import edu.uniquindio.co.pokedex.domain.model.PokemonPage
import edu.uniquindio.co.pokedex.domain.model.PokemonSpecies
import edu.uniquindio.co.pokedex.domain.model.PokemonSummary
import edu.uniquindio.co.pokedex.domain.model.PokemonType
import edu.uniquindio.co.pokedex.domain.repository.PokemonRepository
import jakarta.inject.Inject
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.HttpException

class PokemonRepositoryImpl @Inject constructor(
    private val api: PokeApiService
) : PokemonRepository {

    private val indexMutex = Mutex()
    private var index: List<PokemonSummary>? = null

    override suspend fun getPokemonPage(limit: Int, offset: Int): PokemonPage =
        api.getPokemonPage(limit = limit, offset = offset).toDomain()

    override suspend fun getPokemonDetail(id: Int): PokemonDetail? =
        notFoundAsNull { api.getPokemon(id.toString()).toDomain() }

    override suspend fun getPokemonSpecies(id: Int): PokemonSpecies? =
        notFoundAsNull { api.getPokemonSpecies(id).toDomain() }

    override suspend fun getPokemonIndex(): List<PokemonSummary> =
        index ?: indexMutex.withLock {
            index ?: api.getPokemonPage(limit = INDEX_LIMIT, offset = 0)
                .toDomain()
                .items
                .sortedBy { it.id }
                .also { index = it }
        }

    override suspend fun getPokemonByType(type: PokemonType): List<PokemonSummary> =
        notFoundAsNull { api.getType(type.apiName).toDomain() }.orEmpty()

    override suspend fun getPokemonByGeneration(generation: Generation): List<PokemonSummary> =
        notFoundAsNull { api.getGeneration(generation.id).toDomain() }.orEmpty()

    private inline fun <T> notFoundAsNull(block: () -> T): T? =
        try {
            block()
        } catch (e: HttpException) {
            if (e.code() == 404) null else throw e
        }
}

private const val INDEX_LIMIT = 2000
