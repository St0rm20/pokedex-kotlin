package edu.uniquindio.co.pokedex.domain.model

data class PokemonPage(
    val items: List<PokemonSummary>,
    val totalCount: Int,
    val hasNext: Boolean
)

/** Tamaño de página de la API y de cada "caja" de la cuadrícula. */
const val BOX_SIZE = 30
