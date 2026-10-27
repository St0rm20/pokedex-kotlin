package edu.uniquindio.co.pokedex.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TypeResponseDto(
    val pokemon: List<TypePokemonSlotDto>
)

@Serializable
data class TypePokemonSlotDto(
    val pokemon: NamedResourceDto
)
