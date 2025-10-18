package com.pht.vntechpc.ui.navigation

import com.pht.vntechpc.R

sealed class Route(val route: String) {
    object Welcome : Route("welcome")
    object Login : Route("login")
    object Signup : Route("signup")
    object Forget : Route("forget")
    object Main : Route("main")
    object Home : Route("home")
    object Search : Route("search")
    object Profile : Route("profile")
    object Settings : Route("settings")
}

open class BottomNavigationItem(val route: String, val title: String, val icon: Int) {
    object Home : BottomNavigationItem(Route.Home.route, "Trang chủ", R.drawable.home_24)
    object Search : BottomNavigationItem(Route.Search.route, "Tìm kiếm", R.drawable.search_24)
    object Profile : BottomNavigationItem(Route.Profile.route, "Cá nhân", R.drawable.person_24)
    object Settings : BottomNavigationItem(Route.Settings.route, "Cài đặt", R.drawable.settings_24)
}

val bottomNavigationItems = listOf(
    BottomNavigationItem.Home,
    BottomNavigationItem.Search,
    BottomNavigationItem.Profile,
    BottomNavigationItem.Settings
)

