package edu.uniquindio.co.pokedex.domain.model

/** Las 9 generaciones, definidas en el cliente. [id] es el valor de `GET /generation/{id}`. */
enum class Generation(val id: Int, val label: String) {
    I(1, "Gen I"),
    II(2, "Gen II"),
    III(3, "Gen III"),
    IV(4, "Gen IV"),
    V(5, "Gen V"),
    VI(6, "Gen VI"),
    VII(7, "Gen VII"),
    VIII(8, "Gen VIII"),
    IX(9, "Gen IX")
}
