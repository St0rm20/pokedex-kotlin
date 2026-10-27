package edu.uniquindio.co.pokedex.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerationResponseDto(
    @SerialName("pokemon_species") val pokemonSpecies: List<NamedResourceDto>
)
