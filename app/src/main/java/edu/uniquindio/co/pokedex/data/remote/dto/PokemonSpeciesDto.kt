package edu.uniquindio.co.pokedex.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonSpeciesDto(
    @SerialName("flavor_text_entries") val flavorTextEntries: List<FlavorTextEntryDto> = emptyList(),
    val genera: List<GenusDto> = emptyList()
)

@Serializable
data class FlavorTextEntryDto(
    @SerialName("flavor_text") val flavorText: String,
    val language: NamedResourceDto
)

@Serializable
data class GenusDto(
    val genus: String,
    val language: NamedResourceDto
)
