package edu.uniquindio.co.pokedex.di

import edu.uniquindio.co.pokedex.data.connectivity.NetworkConnectivityObserver
import edu.uniquindio.co.pokedex.domain.repository.ConnectivityObserver
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConnectivityModule {

    @Binds
    @Singleton
    abstract fun bindConnectivityObserver(
        networkConnectivityObserver: NetworkConnectivityObserver
    ): ConnectivityObserver
}
