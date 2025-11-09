package com.pht.vntechpc.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pht.vntechpc.ui.screen.BuildPCScreen
import com.pht.vntechpc.ui.screen.HomeScreen
import com.pht.vntechpc.ui.screen.ProfileScreen
import com.pht.vntechpc.ui.screen.SearchScreen

@Composable
fun BottomNavigation(navController: NavHostController, rootNavController: NavHostController) {
    NavHost(
        navController = navController, startDestination = Route.Home.route,
    ) {
        composable(Route.Home.route) {
            HomeScreen(rootNavController)
        }
        composable(Route.Search.route) {
            SearchScreen()
        }
        composable(Route.BuildPC.route) {
            BuildPCScreen()
        }
        composable(Route.Profile.route) {
            ProfileScreen(rootNavController)
        }
    }
}