package edu.uniquindio.co.pokedex.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import edu.uniquindio.co.pokedex.features.boxes.BoxesScreen
import edu.uniquindio.co.pokedex.features.detail.DetailScreen
import kotlinx.serialization.Serializable

@Serializable
data object BoxesRoute

@Serializable
data class DetailRoute(val id: Int)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = BoxesRoute) {
        composable<BoxesRoute> {
            BoxesScreen(
                onNavigateToDetail = { id -> navController.navigate(DetailRoute(id)) }
            )
        }
        composable<DetailRoute> {
            val route = it.toRoute<DetailRoute>()
            DetailScreen(
                id = route.id,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
