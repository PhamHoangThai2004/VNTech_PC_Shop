package com.pht.vntechpc.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.pht.vntechpc.ui.screen.AccountScreen
import com.pht.vntechpc.ui.screen.AddressFormScreen
import com.pht.vntechpc.ui.screen.AddressScreen
import com.pht.vntechpc.ui.screen.CartScreen
import com.pht.vntechpc.ui.screen.ChangePasswordScreen
import com.pht.vntechpc.ui.screen.EditProfileScreen
import com.pht.vntechpc.ui.screen.ForgetScreen
import com.pht.vntechpc.ui.screen.LoginScreen
import com.pht.vntechpc.ui.screen.MainScreen
import com.pht.vntechpc.ui.screen.OrderDetailScreen
import com.pht.vntechpc.ui.screen.OrdersScreen
import com.pht.vntechpc.ui.screen.PaymentScreen
import com.pht.vntechpc.ui.screen.ProductScreen
import com.pht.vntechpc.ui.screen.SettingsScreen
import com.pht.vntechpc.ui.screen.SignupScreen
import com.pht.vntechpc.ui.screen.StartupScreen

@Composable
fun RootNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = Graph.Auth.graph,
    ) {
        authNavGraph(navController)
        mainNavGraph(navController)
        addressNavGraph(navController)
        orderNavGraph(navController)
    }
}

fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation(startDestination = Route.Startup.route, route = Graph.Auth.graph) {
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
    }
}

fun NavGraphBuilder.mainNavGraph(navController: NavController) {
    navigation(startDestination = Route.Main.route, route = Graph.Main.graph) {
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
        composable(Route.EditProfile.route) {
            EditProfileScreen(navController)
        }
        composable(Route.ChangePassword.route) {
            ChangePasswordScreen(navController)
        }
        composable(Route.Product.route) {
            ProductScreen(navController)
        }
        composable(Route.Payment.route) {
            PaymentScreen(navController)
        }
    }
}

fun NavGraphBuilder.addressNavGraph(navController: NavController) {
    navigation(startDestination = Route.Address.route, route = Graph.Address.graph) {
        composable(Route.Address.route) {
            AddressScreen(navController)
        }
        composable(Route.AddressForm.route) {
            AddressFormScreen(navController)
        }
    }
}

fun NavGraphBuilder.orderNavGraph(navController: NavController) {
    navigation(startDestination = Route.Order.route, route = Graph.Order.graph) {
        composable(Route.Order.route) {
            OrdersScreen(navController)
        }
        composable(Route.OrderDetail.route) {
            OrderDetailScreen(navController)
        }
    }
}
