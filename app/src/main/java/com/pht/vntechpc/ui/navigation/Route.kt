package com.pht.vntechpc.ui.navigation

import com.pht.vntechpc.R

sealed class Route(val route: String) {
    object Startup : Route("startup")
    object Login : Route("login")
    object Signup : Route("signup")
    object Forget : Route("forget")
    object Main : Route("main")
    object Home : Route("home")
    object Search : Route("search")
    object BuildPC : Route("build_pc")
    object Profile : Route("profile")
    object Cart: Route("cart")
    object Settings: Route("settings")
    object Order: Route("order")
    object OrderDetail: Route("order_detail")
    object Address: Route("address")
    object Account: Route("account")
    object EditProfile: Route("edit_profile")
    object ChangePassword: Route("change_password")
    object AddressForm: Route("address_form")
    object Product: Route("product")
    object Payment: Route("payment")
}

open class BottomNavigationItem(val route: String, val title: String, val icon: Int) {
    object Home : BottomNavigationItem(Route.Home.route, "Trang chủ", R.drawable.home_24)
    object Search : BottomNavigationItem(Route.Search.route, "Tìm kiếm", R.drawable.search_24)
    object BuildPC : BottomNavigationItem(Route.BuildPC.route, "Xây dựng PC", R.drawable.device_24)
    object Profile : BottomNavigationItem(Route.Profile.route, "Cá nhân", R.drawable.person_24)
}

val bottomNavigationItems = listOf(
    BottomNavigationItem.Home,
    BottomNavigationItem.Search,
    BottomNavigationItem.BuildPC,
    BottomNavigationItem.Profile
)