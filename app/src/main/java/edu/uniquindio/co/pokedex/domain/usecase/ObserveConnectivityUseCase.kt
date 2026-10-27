package edu.uniquindio.co.pokedex.domain.usecase

import edu.uniquindio.co.pokedex.domain.repository.ConnectivityObserver
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveConnectivityUseCase @Inject constructor(
    private val observer: ConnectivityObserver
) {
    operator fun invoke(): Flow<Boolean> = observer.observe()
}
