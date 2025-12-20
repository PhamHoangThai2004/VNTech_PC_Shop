package com.pht.vntechpc.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pht.vntechpc.ui.navigation.BottomNavigation
import com.pht.vntechpc.ui.navigation.bottomNavigationItems
import com.pht.vntechpc.ui.theme.Background
import com.pht.vntechpc.ui.theme.Selected
import com.pht.vntechpc.ui.theme.Unselected

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(rootNavController: NavController) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            BottomNavigation(navController, rootNavController as NavHostController)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    NavigationBar(
        modifier = Modifier.shadow(8.dp),
        containerColor = Background,
    ) {
        bottomNavigationItems.forEach { item ->
            NavigationBarItem(
                selected = currentDestination?.route == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = {
                    Text(text = item.title, fontWeight = FontWeight.Medium)
                },
                icon = {
                    Icon(painter = painterResource(id = item.icon), contentDescription = null)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Selected,
                    selectedTextColor = Selected,
                    indicatorColor = Background,
                    unselectedIconColor = Unselected,
                    unselectedTextColor = Unselected
                )
            )
        }
    }
}