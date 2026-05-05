package com.juliansellanes.lab4_ex1.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.juliansellanes.lab4_ex1.ui.screens.JulianScreen
import com.juliansellanes.lab4_ex1.ui.screens.MainScreen
import com.juliansellanes.lab4_ex1.ui.screens.SellanesScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(navController = navController)
        }

        composable(
            route = "julian/{categoryId}",
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            JulianScreen(
                navController = navController,
                categoryId = backStackEntry.arguments?.getString("categoryId")
            )
        }

        composable(
            route = "partner/{attractionId}",
            arguments = listOf(
                navArgument("attractionId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            SellanesScreen(
                navController = navController,
                attractionId = backStackEntry.arguments?.getString("attractionId")
            )
        }
    }
}