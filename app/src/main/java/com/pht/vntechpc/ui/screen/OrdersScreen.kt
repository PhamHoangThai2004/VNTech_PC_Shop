package com.pht.vntechpc.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.pht.vntechpc.R
import com.pht.vntechpc.domain.model.OrderShort
import com.pht.vntechpc.ui.component.LoadingDialog
import com.pht.vntechpc.ui.component.MessageDialog
import com.pht.vntechpc.ui.component.OrderItemCard
import com.pht.vntechpc.ui.navigation.Route
import com.pht.vntechpc.ui.theme.Background
import com.pht.vntechpc.ui.theme.DarkBackground
import com.pht.vntechpc.ui.theme.IconOnPrimary
import com.pht.vntechpc.ui.theme.TextOnPrimary
import com.pht.vntechpc.utils.EnumConst
import com.pht.vntechpc.viewmodel.OrderState
import com.pht.vntechpc.viewmodel.OrderUIState
import com.pht.vntechpc.viewmodel.OrderViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(navController: NavController, viewModel: OrderViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getMyOrder()
    }

    HandleOrderSideEffects(state, viewModel, navController)

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text(text = "Đơn hàng", fontWeight = FontWeight.Medium) },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            if (state.orders.isNotEmpty()) {
                OrderList(items = state.orders, onItemClicked = {
                    viewModel.getOrderById(it)
                })
            } else {
                Text(
                    text = "Không có đơn hàng nào",
                    color = TextOnPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun OrderList(items: List<OrderShort>, onItemClicked: (Int) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(items.size) { index ->
            OrderItemCard(items[index], onClick = {
                onItemClicked(items[index].orderId)
                Log.d("BBB", "OrderItemCard: ${items[index].orderId}")
            })
        }
    }
}

@Composable
private fun HandleOrderSideEffects(
    state: OrderUIState,
    viewModel: OrderViewModel,
    navController: NavController
) {
    when (val status = state.status) {
        is OrderState.Failure -> MessageDialog(message = status.message, onAction = {
            viewModel.resetState()
        })

        is OrderState.Loading -> LoadingDialog(message = status.message)
        OrderState.None -> Unit
        is OrderState.Success -> {
            navController.currentBackStackEntry?.savedStateHandle?.set(
                EnumConst.ORDER_KEY,
                status.order
            )
            navController.navigate(Route.OrderDetail.route)
            viewModel.resetState()
        }
    }
}