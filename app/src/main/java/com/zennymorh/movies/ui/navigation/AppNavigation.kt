package com.zennymorh.movies.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zennymorh.movies.ui.screens.PopularMoviesScreen

enum class Screens {
    PopularMovies,
    PopularMovieDetail,
}

sealed class NavigationItem(val route: String) {
    data object PopularMovies : NavigationItem(Screens.PopularMovies.name)
    data object PopularMovieDetail : NavigationItem(Screens.PopularMovieDetail.name)
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = NavigationItem.PopularMovies.route,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavigationItem.PopularMovies.route) {
            PopularMoviesScreen()
        }
    }
}
