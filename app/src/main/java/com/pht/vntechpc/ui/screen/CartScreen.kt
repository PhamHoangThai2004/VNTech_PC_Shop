package com.pht.vntechpc.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.pht.vntechpc.R
import com.pht.vntechpc.domain.model.CartItem
import com.pht.vntechpc.ui.component.CartItemCard
import com.pht.vntechpc.ui.component.ConfirmDialog
import com.pht.vntechpc.ui.component.LoadingDialog
import com.pht.vntechpc.ui.component.MessageDialog
import com.pht.vntechpc.ui.component.OutlinedButtonComponent
import com.pht.vntechpc.ui.navigation.Route
import com.pht.vntechpc.ui.theme.Background
import com.pht.vntechpc.ui.theme.DarkBackground
import com.pht.vntechpc.ui.theme.IconOnPrimary
import com.pht.vntechpc.ui.theme.TextOnPrimary
import com.pht.vntechpc.ui.theme.TextPrimary
import com.pht.vntechpc.utils.EnumConst
import com.pht.vntechpc.viewmodel.CartState
import com.pht.vntechpc.viewmodel.CartUiState
import com.pht.vntechpc.viewmodel.CartViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, viewModel: CartViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.getCart()
    }
    val state by viewModel.uiState.collectAsState()

    HandleCartSideEffects(state, viewModel, navController)

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text(text = "Giỏ hàng", fontWeight = FontWeight.Medium) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = TextOnPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painterResource(R.drawable.arrow_back_24),
                            tint = IconOnPrimary,
                            contentDescription = null,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.confirmClearCart() }) {
                        Icon(
                            painter = painterResource(R.drawable.delete_24),
                            contentDescription = null,
                            tint = IconOnPrimary
                        )
                    }
                }
            )
        },
        bottomBar = {
            val cartItems = state.cart?.cartItems
            if (!cartItems.isNullOrEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    OutlinedButtonComponent(
                        onClick = { viewModel.getSelectedCartItems() },
                        "Mua hàng",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
        ) {
            val cartItems = state.cart?.cartItems
            if (cartItems != null && cartItems.isNotEmpty()) {
                CartList(cartItems, viewModel)
            } else {
                Text(
                    "Giỏ hàng trống",
                    fontSize = 16.sp,
                    color = TextPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun CartList(cartItems: List<CartItem>, viewModel: CartViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(cartItems.size) { index ->
            CartItemCard(
                cartItem = cartItems[index],
                onQuantityChange = { newQuantity ->
                    viewModel.updateQuantity(cartItems[index].id, newQuantity)
                },
                onRemoveItem = {
                    viewModel.removeItem(cartItems[index].id)
                },
                onToggleSelected = {
                    viewModel.toggleSelectedItem(cartItems[index].id, it)
                }
            )
        }
    }
}

@Composable
private fun HandleCartSideEffects(
    state: CartUiState,
    viewModel: CartViewModel,
    navController: NavController
) {
    when (val status = state.status) {
        is CartState.Failure -> {
            MessageDialog(message = status.message, onAction = { viewModel.resetState() })
        }
        is CartState.ConfirmClear -> {
            ConfirmDialog(
                message = status.message, onConfirm = { viewModel.clearCart() },
                onDismiss = { viewModel.resetState() })
        }
        is CartState.Loading -> {
            LoadingDialog(message = status.message)
        }
        is CartState.None -> Unit
        is CartState.Success -> {
            navController.currentBackStackEntry?.savedStateHandle?.set(
                EnumConst.CART_ITEMS_KEY,
                status.cartItems
            )
            navController.navigate(Route.Payment.route)
            viewModel.resetState()
        }
    }
}