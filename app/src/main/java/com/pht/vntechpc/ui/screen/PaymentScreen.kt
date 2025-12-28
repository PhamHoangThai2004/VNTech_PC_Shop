package com.pht.vntechpc.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color
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
import com.pht.vntechpc.domain.model.CartItem
import com.pht.vntechpc.domain.model.PaymentMethod
import com.pht.vntechpc.ui.component.AddressItemSelectCard
import com.pht.vntechpc.ui.component.FilledButtonComponent
import com.pht.vntechpc.ui.component.HorizontalSpacer
import com.pht.vntechpc.ui.component.LoadingDialog
import com.pht.vntechpc.ui.component.MessageDialog
import com.pht.vntechpc.ui.component.PaymentMethodItemCard
import com.pht.vntechpc.ui.component.VerticalSpacer
import com.pht.vntechpc.ui.theme.Background
import com.pht.vntechpc.ui.theme.Border
import com.pht.vntechpc.ui.theme.CardBackground
import com.pht.vntechpc.ui.theme.DarkBackground
import com.pht.vntechpc.ui.theme.IconOnPrimary
import com.pht.vntechpc.ui.theme.Inactive
import com.pht.vntechpc.ui.theme.TextOnPrimary
import com.pht.vntechpc.ui.theme.TextSalePrice
import com.pht.vntechpc.utils.EnumConst
import com.pht.vntechpc.utils.StringFormat
import com.pht.vntechpc.viewmodel.PaymentState
import com.pht.vntechpc.viewmodel.PaymentStatePage
import com.pht.vntechpc.viewmodel.PaymentUiState
import com.pht.vntechpc.viewmodel.PaymentViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    navController: NavController,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getDefaultAddress()
        viewModel.getPaymentMethods()
        val cartItems =
            navController.previousBackStackEntry?.savedStateHandle?.get<List<CartItem>>(EnumConst.CART_ITEMS_KEY)
        if (!cartItems.isNullOrEmpty())
            viewModel.setCartItems(cartItems)
    }

    HandlePaymentSideEffects(state, viewModel, navController)

    Scaffold(
        containerColor = when (state.page) {
            PaymentStatePage.ConfirmOrder -> CardBackground
            PaymentStatePage.SelectAddress -> Background
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (state.page) {
                            PaymentStatePage.ConfirmOrder -> "Thanh toán"
                            PaymentStatePage.SelectAddress -> "Chọn địa chỉ"
                        },
                        fontWeight = FontWeight.Medium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = TextOnPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        when (state.page) {
                            PaymentStatePage.ConfirmOrder ->
                                navController.popBackStack()

                            PaymentStatePage.SelectAddress -> viewModel.backToConfirmOrder()
                        }
                    }) {
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
            if (state.page is PaymentStatePage.ConfirmOrder) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Tổng cộng: ${StringFormat.formatPrice(0)}")
                        HorizontalSpacer(16)
                        FilledButtonComponent(onClick = {
                            viewModel.createOrder()
                        }, content = "Đặt hàng", modifier = Modifier.height(50.dp))
                    }
                }
            }
        }
    ) { innerPadding ->
        when (state.page) {
            PaymentStatePage.ConfirmOrder -> ConfirmOrderPage(state, innerPadding, viewModel)
            PaymentStatePage.SelectAddress -> SelectedAddressPage(state, innerPadding, viewModel)
        }
    }
}

@Composable
private fun ConfirmOrderPage(
    state: PaymentUiState,
    innerPadding: PaddingValues,
    viewModel: PaymentViewModel
) {
    if (!state.cartItems.isNullOrEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                DeliveryAddressLayout(
                    state.address,
                    onClick = { viewModel.goToSelectAddress() })
            }
            item { VerticalSpacer(8) }
            item { CartItemsLayout(state.cartItems) }
            item { VerticalSpacer(8) }
            item {
                PaymentMethodsLayout(
                    state.paymentMethods,
                    state.paymentMethodCode
                ) { viewModel.selectPaymentMethod(it) }
            }
            item { VerticalSpacer(8) }
            item { PaymentDetailLayout(state) }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("Lỗi lấy dữ liệu")
        }
    }
}

@Composable
private fun SelectedAddressPage(
    state: PaymentUiState,
    innerPadding: PaddingValues,
    viewModel: PaymentViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.getAddresses()
    }

    val addresses = state.addresses
    if (addresses.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Không có địa chỉ nào")
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            val selectedAddressId = state.address?.id ?: -1
            items(addresses.size) { it ->
                AddressItemSelectCard(
                    addresses[it],
                    selectedAddressId == addresses[it].id,
                    onClick = {
                        viewModel.selectAddress(it)
                        viewModel.backToConfirmOrder()
                    })
            }
        }
    }
}

@Composable
private fun DeliveryAddressLayout(address: Address?, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 7.dp, bottomEnd = 7.dp))
            .background(Background)
            .padding(16.dp)
            .clickable(
                onClick = { onClick() },
                indication = null,
                interactionSource = null
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (address != null) {
            Column {
                Row {
                    Icon(
                        painterResource(R.drawable.outline_location_on_24),
                        contentDescription = null
                    )
                    Text(address.recipientName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    HorizontalSpacer(8)
                    Text(StringFormat.formatPhoneNumber(address.phoneNumber))
                }
                Text("${address.addressDetail}, ${address.ward}, ${address.district}, ${address.province}")
            }
        } else {
            Text("Chưa có địa chỉ nhận hàng")
        }
        Icon(
            painterResource(R.drawable.chevron_right_24),
            contentDescription = null,
            tint = Color.Black
        )
    }
}

@Composable
private fun PaymentMethodsLayout(
    paymentMethods: List<PaymentMethod>,
    code: String?,
    onChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(7.dp))
            .background(Background)
            .padding(16.dp)
    ) {
        Text("Phương thức thanh toán", fontWeight = FontWeight.Bold)
        VerticalSpacer(8)
        for (paymentMethod in paymentMethods) {
            PaymentMethodItemCard(
                paymentMethod, paymentMethod.code == code
            ) { onChange(it) }
        }
    }
}

@Composable
private fun PaymentDetailLayout(state: PaymentUiState) {
    val totalPrice = state.cartItems?.sumOf { it.price * it.quantity } ?: 0
    val finalPrice = state.shipping?.shippingFee?.let { totalPrice + it } ?: 0
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 7.dp, bottomEnd = 7.dp))
            .background(Background)
            .padding(16.dp)
    ) {
        Text("Chi tiết thanh toán", fontWeight = FontWeight.Bold)
        VerticalSpacer(8)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Tổng tiền hàng")
            Text(StringFormat.formatPrice(totalPrice))
        }
        VerticalSpacer(8)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Phí vận chuyển")
            Text(
                if (state.shipping?.isFreeShipping ?: false) {
                    "Miễn phí"
                } else {
                    StringFormat.formatPrice(state.shipping?.shippingFee ?: 0)
                }
            )
        }
        VerticalSpacer(8)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Giảm giá")
            Text("-${StringFormat.formatPrice(0)}")
        }
        VerticalSpacer(8)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Tổng", fontWeight = FontWeight.W500)
            Text(
                StringFormat.formatPrice(finalPrice),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Inactive
            )
        }
    }

}

@Composable
private fun CartItemsLayout(cartItems: List<CartItem>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(7.dp))
            .background(Background)
            .padding(16.dp)
    ) {
        for (cartItem in cartItems) {
            Column(
                Modifier.clickable(
                    onClick = {
                    },
                    indication = null,
                    interactionSource = null
                )
            ) {
                Row {
                    AsyncImage(
                        model = cartItem.product.mainImage,
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
                            cartItem.product.productName,
                            maxLines = 2,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(cartItem.product.model)
                    }
                }
                VerticalSpacer(8)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Số lượng: ${cartItem.quantity}")
                    Text(
                        StringFormat.formatPrice(cartItem.price),
                        color = TextSalePrice,
                        fontSize = 18.sp, fontWeight = FontWeight.W600
                    )
                }
            }
            if (cartItem != cartItems.last()) {
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
private fun HandlePaymentSideEffects(
    state: PaymentUiState,
    viewModel: PaymentViewModel,
    navController: NavController
) {
    when (val status = state.status) {
        PaymentState.None -> Unit
        is PaymentState.Failure -> MessageDialog(
            message = status.message,
            onAction = { viewModel.resetState() })

        is PaymentState.Loading -> LoadingDialog(message = status.message)
        is PaymentState.Success -> {
            MessageDialog(message = status.message, onAction = {
                navController.popBackStack()
            })
        }
    }
}