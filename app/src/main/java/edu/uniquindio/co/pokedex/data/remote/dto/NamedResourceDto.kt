package edu.uniquindio.co.pokedex.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class NamedResourceDto(
    val name: String,
    val url: String
)
