package edu.uniquindio.co.pokedex.data.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import edu.uniquindio.co.pokedex.domain.repository.ConnectivityObserver
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class NetworkConnectivityObserver @Inject constructor(
    @ApplicationContext context: Context
) : ConnectivityObserver {

    private val connectivityManager = context.getSystemService(ConnectivityManager::class.java)

    override fun observe(): Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
                trySend(capabilities.hasInternet())
            }

            override fun onLost(network: Network) {
                trySend(false)
            }
        }
        val current = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        trySend(current?.hasInternet() == true)
        connectivityManager.registerDefaultNetworkCallback(callback)
        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }.distinctUntilChanged()

    private fun NetworkCapabilities.hasInternet(): Boolean =
        hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}
