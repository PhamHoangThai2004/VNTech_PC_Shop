package com.pht.vntechpc.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pht.vntechpc.R
import com.pht.vntechpc.ui.component.OutlinedButtonComponent
import com.pht.vntechpc.ui.navigation.Route
import com.pht.vntechpc.ui.theme.Background
import com.pht.vntechpc.ui.theme.Border
import com.pht.vntechpc.ui.theme.DarkBackground
import com.pht.vntechpc.ui.theme.IconOnPrimary
import com.pht.vntechpc.ui.theme.IconPrimary
import com.pht.vntechpc.ui.theme.TextOnPrimary
import com.pht.vntechpc.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(rootNavController: NavController, viewModel: ProfileViewModel = hiltViewModel()) {
    val user by viewModel.user.collectAsState()

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text(text = "Cá nhân", fontWeight = FontWeight.Medium) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = TextOnPrimary
                ),
                actions = {
                    IconButton(onClick = { rootNavController.navigate(Route.Cart.route) }) {
                        Icon(
                            painter = painterResource(R.drawable.shopping_cart_24),
                            tint = IconOnPrimary,
                            contentDescription = null,
                        )
                    }

                    IconButton(onClick = { rootNavController.navigate(Route.Settings.route) }) {
                        Icon(
                            painter = painterResource(R.drawable.settings_24),
                            tint = IconOnPrimary,
                            contentDescription = null,
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "Chào mừng, ${user?.fullName ?: "Người dùng"}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(2.dp, Border, CircleShape)
            ) {
                AsyncImage(
                    model = user?.avatar,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    placeholder = painterResource(R.drawable.default_avatar),
                    error = painterResource(R.drawable.default_avatar)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            profileItems.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .clickable(
                            onClick = {
                                when (item) {
                                    is ProfileItem.Orders -> {
                                        rootNavController.navigate(Route.Order.route)
                                    }

                                    is ProfileItem.Address -> {
                                        rootNavController.navigate(Route.Address.route)
                                    }

                                    is ProfileItem.Account -> {
                                        rootNavController.navigate(Route.Account.route)
                                    }

                                    is ProfileItem.Notification -> {
                                        Log.d("BBB", "Notification clicked")
                                    }
                                }
                            },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = null,
                        tint = IconPrimary
                    )
                    Text(
                        text = item.title,
                        modifier = Modifier.padding(start = 12.dp),
                        fontWeight = FontWeight.Medium
                    )
                }
                HorizontalDivider(thickness = 1.dp, color = Border)

                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButtonComponent(
                onClick = {
                    viewModel.logout()
                    rootNavController.navigate(Route.Login.route) {
                        popUpTo(Route.Main.route) {
                            inclusive = true
                        }
                    }
                },
                content = "Đăng xuất",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }
    }
}

open class ProfileItem(val title: String, val icon: Int) {
    object Orders : ProfileItem("Đơn hàng", R.drawable.orders_24)
    object Address : ProfileItem("Địa chỉ", R.drawable.outline_location_on_24)
    object Account : ProfileItem("Quản lý Tài khoản", R.drawable.person_24)
    object Notification : ProfileItem("Thông báo", R.drawable.email_24)
}

val profileItems = listOf(
    ProfileItem.Orders, ProfileItem.Address, ProfileItem.Account, ProfileItem.Notification
)