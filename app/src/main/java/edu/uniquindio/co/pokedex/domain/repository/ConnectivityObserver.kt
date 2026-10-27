package edu.uniquindio.co.pokedex.domain.repository

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    /** Emite true cuando hay internet y false cuando se pierde; solo emite al cambiar. */
    fun observe(): Flow<Boolean>
}
