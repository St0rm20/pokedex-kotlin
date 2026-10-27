package edu.uniquindio.co.pokedex.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PokemonListResponseDto(
    val count: Int,
    val next: String? = null,
    val results: List<NamedResourceDto>
)
