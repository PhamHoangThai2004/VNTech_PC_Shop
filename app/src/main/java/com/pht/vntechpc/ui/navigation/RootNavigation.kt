package com.pht.vntechpc.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pht.vntechpc.ui.screen.AccountScreen
import com.pht.vntechpc.ui.screen.AddressScreen
import com.pht.vntechpc.ui.screen.CartScreen
import com.pht.vntechpc.ui.screen.ForgetScreen
import com.pht.vntechpc.ui.screen.LoginScreen
import com.pht.vntechpc.ui.screen.MainScreen
import com.pht.vntechpc.ui.screen.OrdersScreen
import com.pht.vntechpc.ui.screen.SettingsScreen
import com.pht.vntechpc.ui.screen.SignupScreen
import com.pht.vntechpc.ui.screen.StartupScreen

@Composable
fun RootNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = Route.Startup.route,
    ) {
        composable(Route.Startup.route) {
            StartupScreen(navController)
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
            MainScreen(navController)
        }
        composable(Route.Cart.route) {
            CartScreen(navController)
        }
        composable(Route.Settings.route) {
            SettingsScreen(navController)
        }
        composable(Route.Order.route) {
            OrdersScreen(navController)
        }
        composable(Route.Address.route) {
            AddressScreen(navController)
        }
        composable(Route.Account.route) {
            AccountScreen(navController)
        }
    }
}