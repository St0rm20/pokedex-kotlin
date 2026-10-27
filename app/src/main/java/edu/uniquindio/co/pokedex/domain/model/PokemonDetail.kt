package edu.uniquindio.co.pokedex.domain.model

data class PokemonDetail(
    val id: Int,
    val name: String,
    val types: List<PokemonType>,
    val heightMeters: Double,
    val weightKg: Double,
    val abilities: List<Ability>,
    val stats: List<Stat>,
    val spriteUrl: String,
    val artworkUrl: String
)

data class Ability(
    val name: String,
    val isHidden: Boolean
)
