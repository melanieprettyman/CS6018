package com.example.makart

import MainMenuScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object MainMenu : Screen("main_menu")
    object DrawEditor : Screen("draw_editor/{drawingId}") {
        fun createRoute(drawingId: Int) = "draw_editor/$drawingId"
    }
}


@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreenContent(onNavigateToMenu = {
                navController.navigate(Screen.MainMenu.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        composable(Screen.MainMenu.route) {
            MainMenuScreen(navController = navController)
        }
        composable(
            route = Screen.DrawEditor.route,
            arguments = listOf(navArgument("drawingId") { type = NavType.IntType })
        ) { backStackEntry ->
            val drawingId = backStackEntry.arguments?.getInt("drawingId") ?: -1
            DrawEditorScreen(
                drawingId = drawingId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}



