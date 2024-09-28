package com.example.makart

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object MainMenu : Screen("main_menu")
    object DrawEditor : Screen("draw_editor")
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
            MainMenuScreen(onDrawClick = {
                navController.navigate(Screen.DrawEditor.route)  // Navigate to DrawEditorScreen
            })
        }
        composable(Screen.DrawEditor.route) {
            DrawEditorScreen(
                onBack = {
                    navController.popBackStack() // Navigate back to MainMenuScreen
                },
            )
        }
    }
}

