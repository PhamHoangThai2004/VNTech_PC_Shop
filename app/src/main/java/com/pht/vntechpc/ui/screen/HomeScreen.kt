package com.pht.vntechpc.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.pht.vntechpc.R
import com.pht.vntechpc.ui.component.FilledButtonComponent
import com.pht.vntechpc.ui.navigation.Route
import com.pht.vntechpc.ui.theme.Background
import com.pht.vntechpc.ui.theme.DarkBackground
import com.pht.vntechpc.ui.theme.IconOnPrimary
import com.pht.vntechpc.ui.theme.TextOnPrimary
import com.pht.vntechpc.viewmodel.HomeViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(rootNavController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text(text = "Trang chủ", fontWeight = FontWeight.Medium) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = TextOnPrimary
                ),
                actions = {
                    IconButton(
                        onClick = { rootNavController.navigate(Route.Cart.route) },
                        content = {
                            Icon(
                                painter = painterResource(R.drawable.shopping_cart_24),
                                tint = IconOnPrimary,
                                contentDescription = null,
                            )
                        }
                    )

                    IconButton(
                        onClick = { },
                        content = {
                            Icon(
                                painter = painterResource(R.drawable.email_24),
                                tint = IconOnPrimary,
                                contentDescription = null,
                            )
                        }
                    )
                }
            )
        }
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Column {
                FilledButtonComponent(onClick = { viewModel.getAllCategory() }, content = "Danh mục")
                FilledButtonComponent(onClick = { viewModel.getCategoryById(1)}, content = "Chi tiết danh mục")
                FilledButtonComponent(onClick = { viewModel.getAllProducts() }, content = "Sản phẩm")
                FilledButtonComponent(onClick = { viewModel.getProductById(2) }, content = "Chi tiết sản phẩm")
            }
        }
    }
}