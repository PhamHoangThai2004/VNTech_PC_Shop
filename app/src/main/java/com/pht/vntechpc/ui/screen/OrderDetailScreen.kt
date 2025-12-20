package com.pht.vntechpc.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pht.vntechpc.R
import com.pht.vntechpc.domain.model.Address
import com.pht.vntechpc.domain.model.Order
import com.pht.vntechpc.domain.model.OrderItem
import com.pht.vntechpc.ui.component.FilledButtonComponent
import com.pht.vntechpc.ui.component.HorizontalSpacer
import com.pht.vntechpc.ui.component.MessageDialog
import com.pht.vntechpc.ui.component.OutlinedButtonComponent
import com.pht.vntechpc.ui.component.VerticalSpacer
import com.pht.vntechpc.ui.navigation.Route
import com.pht.vntechpc.ui.theme.Background
import com.pht.vntechpc.ui.theme.Border
import com.pht.vntechpc.ui.theme.CardBackground
import com.pht.vntechpc.ui.theme.DarkBackground
import com.pht.vntechpc.ui.theme.IconOnPrimary
import com.pht.vntechpc.ui.theme.Inactive
import com.pht.vntechpc.ui.theme.TextOnPrimary
import com.pht.vntechpc.ui.theme.TextSalePrice
import com.pht.vntechpc.utils.EnumConst
import com.pht.vntechpc.utils.EnumOrderStatus
import com.pht.vntechpc.utils.StringFormat
import com.pht.vntechpc.viewmodel.OrderDetailState
import com.pht.vntechpc.viewmodel.OrderDetailUIState
import com.pht.vntechpc.viewmodel.OrderDetailViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    navController: NavController,
    viewModel: OrderDetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        val order =
            navController.previousBackStackEntry?.savedStateHandle?.get<Order>(EnumConst.ORDER_KEY)
        if (order != null)
            viewModel.setOrder(order)
    }

    HandleOrderDetailSideEffects(state, viewModel, navController)

    Scaffold(
        containerColor = CardBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = EnumOrderStatus.toTitle(state.order?.status ?: ""),
                        fontWeight = FontWeight.Medium
                    )
                },
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
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center,
            ) {
                val modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                if (state.order?.canBeCancelled ?: false) {
                    OutlinedButtonComponent(onClick = {}, "Huỷ đơn hàng", modifier = modifier)
                } else {
                    FilledButtonComponent(
                        onClick = {},
                        "Mua lại",
                        modifier = modifier
                    )
                }

            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            if (state.order != null) {
                LazyColumn {
                    val date = when (state.order!!.status) {
                        EnumOrderStatus.CONFIRMED -> state.order!!.confirmedAt
                        EnumOrderStatus.DELIVERED -> state.order!!.deliveredAt
                        EnumOrderStatus.CANCELLED -> state.order!!.cancelledAt
                        else -> state.order!!.createdAt
                    }
                    item {
                        DeliveryAddressLayout(state.order!!.address, state.order!!.status, date!!)
                    }
                    item { VerticalSpacer(8) }
                    item { OrderItemsLayout(state.order!!.orderItems, viewModel) }
                    item { VerticalSpacer(8) }
                    item {
                        OrderInfoLayout(
                            state.order!!.totalPrice,
                            state.order!!.shippingFee,
                            state.order!!.discount,
                            state.order!!.finalPrice
                        )
                    }
                    item { VerticalSpacer(8) }
                    item { OrderDetailLayout(order = state.order!!) }
                }

            } else {
                Text("Lỗi lấy dữ liệu")
            }
        }
    }
}

@Composable
private fun DeliveryAddressLayout(address: Address, orderStatus: String, orderDate: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 7.dp, bottomEnd = 7.dp))
            .background(Background)
            .padding(16.dp)
    ) {
        Text(
            EnumOrderStatus.toStatus(orderStatus),
            fontWeight = FontWeight.W500,
            color = EnumOrderStatus.toColor(orderStatus)
        )
        Text(EnumOrderStatus.toOrderStatusInfo(orderStatus, orderDate))
        VerticalSpacer(16)
        HorizontalDivider(
            modifier = Modifier.height(1.dp),
            thickness = 1.dp,
            color = Border.copy(alpha = 0.5f)
        )
        VerticalSpacer(16)
        Row(modifier = Modifier.fillMaxWidth()) {
            Icon(painterResource(R.drawable.outline_location_on_24), contentDescription = null)
            Text(address.recipientName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            HorizontalSpacer(8)
            Text(StringFormat.formatPhoneNumber(address.phoneNumber))
        }
        Text("${address.addressDetail}, ${address.ward}, ${address.district}, ${address.province}")
    }
}

@Composable
private fun OrderItemsLayout(orderItems: List<OrderItem>, viewModel: OrderDetailViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(7.dp))
            .background(Background)
            .padding(16.dp)
    ) {
        for (orderItem in orderItems) {
            Column(
                Modifier.clickable(
                    onClick = {
                        viewModel.getProductDetail(orderItem.productId)
                    },
                    indication = null,
                    interactionSource = null
                )
            ) {
                Row {
                    AsyncImage(
                        model = orderItem.mainImage,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        placeholder = painterResource(R.drawable.app_logo),
                        error = painterResource(R.drawable.app_logo)
                    )
                    HorizontalSpacer(8)
                    Column {
                        Text(
                            orderItem.productName,
                            maxLines = 2,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(orderItem.model)
                    }
                }
                VerticalSpacer(8)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Số lượng: ${orderItem.quantity}")
                    Text(
                        StringFormat.formatPrice(orderItem.totalPrice),
                        color = TextSalePrice,
                        fontSize = 18.sp, fontWeight = FontWeight.W600
                    )
                }
            }
            if (orderItem != orderItems.last()) {
                VerticalSpacer(16)
                HorizontalDivider(
                    modifier = Modifier.height(1.dp),
                    thickness = 1.dp,
                    color = Border.copy(alpha = 0.5f)
                )
                VerticalSpacer(16)
            }
        }
    }
}

@Composable
private fun OrderInfoLayout(
    totalPrice: Long,
    shippingFee: Long,
    discount: Long,
    finalPayment: Long
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 7.dp, bottomEnd = 7.dp))
            .background(Background)
            .padding(16.dp)
    ) {
        Text("Tổng quan đơn hàng", fontWeight = FontWeight.Bold)
        VerticalSpacer(8)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Tổng tiền")
            Text(StringFormat.formatPrice(totalPrice))
        }
        VerticalSpacer(8)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Phí vận chuyển")
            Text(StringFormat.formatPrice(shippingFee))
        }
        VerticalSpacer(8)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Giảm giá")
            Text("-${StringFormat.formatPrice(discount)}")
        }
        VerticalSpacer(8)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Tổng", fontWeight = FontWeight.W500)
            Text(
                StringFormat.formatPrice(finalPayment),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Inactive
            )
        }
    }
}

@Composable
private fun OrderDetailLayout(order: Order) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 7.dp, bottomEnd = 7.dp))
            .background(Background)
            .padding(16.dp)
    ) {
        Text("Chi tiết đơn hàng", fontWeight = FontWeight.Bold)
        VerticalSpacer(8)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Mã đơn hàng")
            Text("#${order.orderCode}", fontWeight = FontWeight.W500)
        }
        VerticalSpacer(8)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Ngày đặt hàng")
            Text(StringFormat.formatTime(order.createdAt))
        }
        VerticalSpacer(8)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Phương thức thanh toán")
            Text(order.paymentMethod ?: "Thanh toán khi nhận hàng")
        }
        if (order.status == EnumOrderStatus.DELIVERED) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Ngày nhận hàng")
                Text(
                    StringFormat.formatDateTime(order.deliveredAt ?: "")
                )
            }
        } else if (order.status == EnumOrderStatus.CANCELLED) {
            VerticalSpacer(8)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Ngày huỷ")
                Text(
                    StringFormat.formatDateTime(order.cancelledAt ?: "")
                )
            }
        }

    }
}

@Composable
private fun HandleOrderDetailSideEffects(
    state: OrderDetailUIState,
    viewModel: OrderDetailViewModel,
    navController: NavController
) {
    when (val status = state.status) {
        is OrderDetailState.Failure -> MessageDialog(
            message = status.message,
            onAction = { viewModel.resetState() })

        is OrderDetailState.Loading -> MessageDialog(
            message = status.message,
            onAction = { viewModel.resetState() })

        OrderDetailState.None -> Unit
        is OrderDetailState.Success -> {
            navController.currentBackStackEntry?.savedStateHandle?.set(
                EnumConst.PRODUCT_KEY,
                status.product)
            navController.navigate(Route.Product.route)
            viewModel.resetState()
        }
    }
}