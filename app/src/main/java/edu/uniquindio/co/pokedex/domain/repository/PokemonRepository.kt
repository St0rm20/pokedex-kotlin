package edu.uniquindio.co.pokedex.domain.repository

import edu.uniquindio.co.pokedex.domain.model.Generation
import edu.uniquindio.co.pokedex.domain.model.PokemonDetail
import edu.uniquindio.co.pokedex.domain.model.PokemonPage
import edu.uniquindio.co.pokedex.domain.model.PokemonSpecies
import edu.uniquindio.co.pokedex.domain.model.PokemonSummary
import edu.uniquindio.co.pokedex.domain.model.PokemonType

interface PokemonRepository {
    suspend fun getPokemonPage(limit: Int, offset: Int): PokemonPage

    /** Devuelve null si el Pokémon no existe (404). */
    suspend fun getPokemonDetail(id: Int): PokemonDetail?

    /** Devuelve null si la especie no existe (404). */
    suspend fun getPokemonSpecies(id: Int): PokemonSpecies?

    /** Índice completo (solo nombre e id), descargado una vez y guardado en memoria. */
    suspend fun getPokemonIndex(): List<PokemonSummary>

    /** Todos los Pokémon de un tipo, ordenados por id. */
    suspend fun getPokemonByType(type: PokemonType): List<PokemonSummary>

    /** Todos los Pokémon de una generación, ordenados por id. */
    suspend fun getPokemonByGeneration(generation: Generation): List<PokemonSummary>
}
