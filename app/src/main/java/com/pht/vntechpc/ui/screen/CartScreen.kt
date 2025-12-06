package com.pht.vntechpc.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.pht.vntechpc.R
import com.pht.vntechpc.ui.component.CartItemCard
import com.pht.vntechpc.ui.component.LoadingDialog
import com.pht.vntechpc.ui.component.MessageDialog
import com.pht.vntechpc.ui.theme.Background
import com.pht.vntechpc.ui.theme.DarkBackground
import com.pht.vntechpc.ui.theme.IconOnPrimary
import com.pht.vntechpc.ui.theme.TextOnPrimary
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


    HandleCartSideEffects(state, viewModel)

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
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            item { Button(onClick = { viewModel.clearCart() }) { Text("Xoá tất cả") } }
            val cartItems = state.cart?.cartItems
            if (cartItems == null || cartItems.isEmpty()) {
                item { Text("Giỏ hàng trống") }
            } else {
                items(cartItems.size) { index ->
                    CartItemCard(
                        cartItem = cartItems[index],
                        onQuantityChange = { newQuantity ->
                            Log.d("BBB", "onQuantityChange: $newQuantity")
                            viewModel.updateQuantity(cartItems[index].id, newQuantity)
                        },
                        onRemoveItem = {
                            viewModel.removeItem(cartItems[index].id)
                        }
                    )
                    if (index < cartItems.size) {

                        Spacer(modifier = Modifier.height(16.dp))

                        HorizontalDivider(
                            Modifier, 1.dp,
                            Color.Black
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun HandleCartSideEffects(
    state: CartUiState,
    viewModel: CartViewModel
) {
    when (val status = state.status) {
        is CartState.Failure -> {
            MessageDialog(message = status.message, onAction = { viewModel.resetState() })
        }

        is CartState.Loading -> {
            LoadingDialog(message = status.message)
        }

        else -> Unit
    }
}