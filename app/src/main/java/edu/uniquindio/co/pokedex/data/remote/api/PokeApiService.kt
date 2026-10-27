package edu.uniquindio.co.pokedex.data.remote.api

import edu.uniquindio.co.pokedex.data.remote.dto.GenerationResponseDto
import edu.uniquindio.co.pokedex.data.remote.dto.PokemonDetailDto
import edu.uniquindio.co.pokedex.data.remote.dto.PokemonListResponseDto
import edu.uniquindio.co.pokedex.data.remote.dto.TypeResponseDto
import edu.uniquindio.co.pokedex.data.remote.dto.PokemonSpeciesDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {

    @GET("pokemon")
    suspend fun getPokemonPage(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonListResponseDto

    @GET("pokemon/{idOrName}")
    suspend fun getPokemon(@Path("idOrName") idOrName: String): PokemonDetailDto

    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(@Path("id") id: Int): PokemonSpeciesDto

    @GET("type/{name}")
    suspend fun getType(@Path("name") name: String): TypeResponseDto

    @GET("generation/{id}")
    suspend fun getGeneration(@Path("id") id: Int): GenerationResponseDto
}
