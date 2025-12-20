package com.pht.vntechpc.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.pht.vntechpc.R
import com.pht.vntechpc.domain.model.Product
import com.pht.vntechpc.ui.component.LoadingDialog
import com.pht.vntechpc.ui.component.MessageDialog
import com.pht.vntechpc.ui.component.ProductItemCart
import com.pht.vntechpc.ui.navigation.Route
import com.pht.vntechpc.ui.theme.Background
import com.pht.vntechpc.ui.theme.DarkBackground
import com.pht.vntechpc.ui.theme.IconOnPrimary
import com.pht.vntechpc.ui.theme.TextOnPrimary
import com.pht.vntechpc.utils.EnumConst
import com.pht.vntechpc.viewmodel.HomeState
import com.pht.vntechpc.viewmodel.HomeUiState
import com.pht.vntechpc.viewmodel.HomeViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(rootNavController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getAllProducts()
    }

    HandleHomeSideEffects(state, viewModel, rootNavController)

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
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            ProductsListComponent(products = state.products, viewModel = viewModel)
        }
    }
}

@Composable
private fun ProductsListComponent(products: List<Product>, viewModel: HomeViewModel) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(products.size) { it ->
            ProductItemCart(
                product = products[it],
                onAddToCart = { viewModel.addProductToCart(it, 1) },
                onProductClick = { viewModel.getProductById(it) }
            )
        }
    }
}

@Composable
private fun HandleHomeSideEffects(
    state: HomeUiState,
    viewModel: HomeViewModel,
    navController: NavController
) {
    when (val status = state.status) {
        is HomeState.Failure -> MessageDialog(
            message = status.message,
            onAction = { viewModel.resetState() })

        is HomeState.AddToCartSuccess -> MessageDialog(
            message = status.message,
            onAction = { viewModel.resetState() })

        is HomeState.Loading -> LoadingDialog(message = status.message)
        is HomeState.None -> Unit
        is HomeState.GetProductDetailSuccess -> {
            navController.currentBackStackEntry?.savedStateHandle?.set(
                EnumConst.PRODUCT_KEY,
                status.product
            )
            navController.navigate(Route.Product.route)
            viewModel.resetState()
        }
    }
}