package edu.uniquindio.co.pokedex.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonDetailDto(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<TypeSlotDto>,
    val abilities: List<AbilitySlotDto>,
    val stats: List<StatSlotDto>
)

@Serializable
data class TypeSlotDto(
    val slot: Int,
    val type: NamedResourceDto
)

@Serializable
data class AbilitySlotDto(
    val ability: NamedResourceDto,
    @SerialName("is_hidden") val isHidden: Boolean = false
)

@Serializable
data class StatSlotDto(
    @SerialName("base_stat") val baseStat: Int,
    val stat: NamedResourceDto
)
