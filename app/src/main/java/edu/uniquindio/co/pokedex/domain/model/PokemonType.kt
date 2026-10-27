package edu.uniquindio.co.pokedex.domain.model

/** Los 18 tipos de PokeAPI. [apiName] es el valor que espera `GET /type/{nombre}`. */
enum class PokemonType(val apiName: String, val label: String) {
    NORMAL("normal", "Normal"),
    FIRE("fire", "Fuego"),
    WATER("water", "Agua"),
    ELECTRIC("electric", "Eléctrico"),
    GRASS("grass", "Planta"),
    ICE("ice", "Hielo"),
    FIGHTING("fighting", "Lucha"),
    POISON("poison", "Veneno"),
    GROUND("ground", "Tierra"),
    FLYING("flying", "Volador"),
    PSYCHIC("psychic", "Psíquico"),
    BUG("bug", "Bicho"),
    ROCK("rock", "Roca"),
    GHOST("ghost", "Fantasma"),
    DRAGON("dragon", "Dragón"),
    DARK("dark", "Siniestro"),
    STEEL("steel", "Acero"),
    FAIRY("fairy", "Hada");

    companion object {
        fun fromApiName(name: String): PokemonType? = entries.firstOrNull { it.apiName == name }
    }
}
