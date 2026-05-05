package com.juliansellanes.lab3_ex2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.juliansellanes.lab3_ex2.ui.screens.AddMovieScreen
import com.juliansellanes.lab3_ex2.ui.screens.EditMovieScreen
import com.juliansellanes.lab3_ex2.ui.screens.HomeScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController = navController)
        }

        composable("add") {
            AddMovieScreen(navController = navController)
        }

        composable(
            route = "edit/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            EditMovieScreen(
                navController = navController,
                movieId = backStackEntry.arguments?.getInt("movieId")
            )
        }
    }
}