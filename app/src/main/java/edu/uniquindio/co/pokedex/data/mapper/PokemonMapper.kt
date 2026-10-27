package edu.uniquindio.co.pokedex.data.mapper

import edu.uniquindio.co.pokedex.data.remote.dto.GenerationResponseDto
import edu.uniquindio.co.pokedex.data.remote.dto.NamedResourceDto
import edu.uniquindio.co.pokedex.data.remote.dto.PokemonDetailDto
import edu.uniquindio.co.pokedex.data.remote.dto.PokemonListResponseDto
import edu.uniquindio.co.pokedex.data.remote.dto.PokemonSpeciesDto
import edu.uniquindio.co.pokedex.data.remote.dto.TypeResponseDto
import edu.uniquindio.co.pokedex.domain.model.Ability
import edu.uniquindio.co.pokedex.domain.model.PokemonDetail
import edu.uniquindio.co.pokedex.domain.model.PokemonPage
import edu.uniquindio.co.pokedex.domain.model.PokemonSummary
import edu.uniquindio.co.pokedex.domain.model.PokemonSpecies
import edu.uniquindio.co.pokedex.domain.model.PokemonType
import edu.uniquindio.co.pokedex.domain.model.Stat

private const val SPRITES_BASE =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon"

fun spriteUrl(id: Int): String = "$SPRITES_BASE/$id.png"

fun artworkUrl(id: Int): String = "$SPRITES_BASE/other/official-artwork/$id.png"

/** Extrae el id del final de una URL como `.../pokemon/25/` o `.../pokemon-species/25/`. */
fun extractIdFromUrl(url: String): Int? =
    url.trimEnd('/').substringAfterLast('/').toIntOrNull()

fun NamedResourceDto.toSummary(): PokemonSummary? {
    val id = extractIdFromUrl(url) ?: return null
    return PokemonSummary(id = id, name = name, spriteUrl = spriteUrl(id))
}

private fun List<NamedResourceDto>.toSortedSummaries(): List<PokemonSummary> =
    mapNotNull { it.toSummary() }.sortedBy { it.id }

fun PokemonListResponseDto.toDomain(): PokemonPage = PokemonPage(
    items = results.mapNotNull { it.toSummary() },
    totalCount = count,
    hasNext = next != null
)

fun TypeResponseDto.toDomain(): List<PokemonSummary> =
    pokemon.map { it.pokemon }.toSortedSummaries()

fun GenerationResponseDto.toDomain(): List<PokemonSummary> =
    pokemonSpecies.toSortedSummaries()

fun PokemonDetailDto.toDomain(): PokemonDetail = PokemonDetail(
    id = id,
    name = name,
    types = types.sortedBy { it.slot }.mapNotNull { PokemonType.fromApiName(it.type.name) },
    // La API entrega altura en decímetros y peso en hectogramos.
    heightMeters = height / 10.0,
    weightKg = weight / 10.0,
    abilities = abilities.map { Ability(name = it.ability.name.toDisplayName(), isHidden = it.isHidden) },
    stats = stats.map { Stat(name = it.stat.name.toStatLabel(), value = it.baseStat) },
    spriteUrl = spriteUrl(id),
    artworkUrl = artworkUrl(id)
)

fun PokemonSpeciesDto.toDomain(): PokemonSpecies = PokemonSpecies(
    genus = genera.firstInLanguage { it.language.name }?.genus,
    description = flavorTextEntries.firstInLanguage { it.language.name }
        ?.flavorText
        ?.let(::cleanFlavorText)
        ?.takeIf { it.isNotEmpty() }
)

/** Primera entrada en español y, si no hay, la primera en inglés. */
private inline fun <T> List<T>.firstInLanguage(language: (T) -> String): T? =
    firstOrNull { language(it) == "es" } ?: firstOrNull { language(it) == "en" }

/** Quita saltos de línea y caracteres de control que trae el texto de la API. */
private fun cleanFlavorText(text: String): String =
    text.replace(Regex("[\\u0000-\\u001F\\u007F]+"), " ")
        .replace(Regex("\\s+"), " ")
        .trim()

private fun String.toDisplayName(): String =
    replace('-', ' ').replaceFirstChar { it.uppercase() }

private fun String.toStatLabel(): String = when (this) {
    "hp" -> "PS"
    "attack" -> "Ataque"
    "defense" -> "Defensa"
    "special-attack" -> "Ataque Esp."
    "special-defense" -> "Defensa Esp."
    "speed" -> "Velocidad"
    else -> toDisplayName()
}
