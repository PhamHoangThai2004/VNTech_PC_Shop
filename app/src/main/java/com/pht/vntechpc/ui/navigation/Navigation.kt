package com.pht.vntechpc.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pht.vntechpc.ui.screen.*

@Composable
fun AuthNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = Route.Welcome.route,
    ) {
        composable(Route.Welcome.route) {
            WelcomeScreen(navController)
        }
        composable(Route.Login.route) {
            LoginScreen(navController)
        }
        composable(Route.Signup.route) {
            SignupScreen(navController)
        }
        composable(Route.Forget.route) {
            ForgetScreen(navController)
        }
        composable(Route.Main.route) {
            MainScreen()
        }
    }
}

@Composable
fun MainNavigation(navController: NavHostController) {
    NavHost(
        navController = navController, startDestination = Route.Home.route,
    ) {
        composable(Route.Home.route) {
            HomeScreen(navController)
        }
        composable(Route.Search.route) {
            SearchScreen(navController)
        }
        composable(Route.Profile.route) {
            ProfileScreen(navController)
        }
        composable(Route.Settings.route) {
            SettingsScreen(navController)
        }
    }
}