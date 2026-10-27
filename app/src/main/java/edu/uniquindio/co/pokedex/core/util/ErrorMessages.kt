package edu.uniquindio.co.pokedex.core.util

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

/** Traduce un error de red a un mensaje legible para el usuario. */
fun Throwable.toUserMessage(): String = when (this) {
    is SocketTimeoutException -> "Tiempo de espera agotado. Inténtalo de nuevo."
    is HttpException -> if (code() >= 500) {
        "Error del servidor. Inténtalo más tarde."
    } else {
        "No se pudo completar la solicitud (código ${code()})."
    }
    is IOException -> "Sin conexión a internet. Revisa tu red."
    else -> "Ocurrió un error inesperado."
}
