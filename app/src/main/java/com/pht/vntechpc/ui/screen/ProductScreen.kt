package com.pht.vntechpc.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pht.vntechpc.R
import com.pht.vntechpc.domain.model.Image
import com.pht.vntechpc.domain.model.Product
import com.pht.vntechpc.domain.model.Specification
import com.pht.vntechpc.ui.component.FilledButtonComponent
import com.pht.vntechpc.ui.component.HorizontalSpacer
import com.pht.vntechpc.ui.component.LoadingDialog
import com.pht.vntechpc.ui.component.MessageDialog
import com.pht.vntechpc.ui.component.RatingBar
import com.pht.vntechpc.ui.component.TextHasSpan
import com.pht.vntechpc.ui.component.VerticalSpacer
import com.pht.vntechpc.ui.theme.Active
import com.pht.vntechpc.ui.theme.Background
import com.pht.vntechpc.ui.theme.Border
import com.pht.vntechpc.ui.theme.CardBackground
import com.pht.vntechpc.ui.theme.DarkBackground
import com.pht.vntechpc.ui.theme.IconOnPrimary
import com.pht.vntechpc.ui.theme.Inactive
import com.pht.vntechpc.ui.theme.Info
import com.pht.vntechpc.ui.theme.TextOnPrimary
import com.pht.vntechpc.ui.theme.TextOriginalPrice
import com.pht.vntechpc.ui.theme.TextPrimary
import com.pht.vntechpc.ui.theme.TextSalePrice
import com.pht.vntechpc.utils.EnumConst
import com.pht.vntechpc.utils.StringFormat
import com.pht.vntechpc.viewmodel.ProductState
import com.pht.vntechpc.viewmodel.ProductUiState
import com.pht.vntechpc.viewmodel.ProductViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    navController: NavController,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        val product =
            navController.previousBackStackEntry?.savedStateHandle?.get<Product>(EnumConst.PRODUCT_KEY)
        if (product != null)
            viewModel.setProduct(product)
    }

    HandleProductSideEffects(state, viewModel)

    Scaffold(
        containerColor = CardBackground,
        topBar = {
            TopAppBar(
                title = {},
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
            if (state.product != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {}) {
                            Icon(
                                painter = painterResource(R.drawable.comment_24),
                                contentDescription = null,
                                tint = Info
                            )
                        }

                        Box(
                            modifier = Modifier
                                .height(30.dp)
                                .width(2.dp)
                                .background(Border)
                        )

                        IconButton(onClick = {
                            viewModel.addProductToCart(state.product!!.id)
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.add_shopping_cart_24),
                                contentDescription = null,
                                tint = Active
                            )
                        }
                    }
                    FilledButtonComponent(
                        onClick = {},
                        "Mua ngay",
                        modifier = Modifier
                            .weight(2f)
                            .height(50.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            if (state.product != null) {
                LazyColumn {
                    item {
                        state.product!!.images?.takeIf { it.isNotEmpty() }?.let {
                            ProductImagesPager(it)
                        }
                    }
                    item { ProductDetailLayout(state.product!!) }
                    if (state.product!!.salePrice != null) {
                        item { VerticalSpacer(10) }
                        item {
                            ProductPriceLayout(
                                state.product!!.originalPrice,
                                state.product!!.salePrice!!,
                                state.product!!.stock
                            )
                        }
                    }
                    if (state.product!!.specifications != null) {
                        item {
                            VerticalSpacer(10)
                            SpecificationsLayout(state.product!!.specifications!!)
                        }
                    }
                }
            } else {
                Text("Lỗi lấy dữ liệu")
            }
        }
    }
}

@Composable
private fun ProductImagesPager(images: List<Image>) {
    val pagerState =
        rememberPagerState(initialPage = images.indexOfFirst { it.main }.takeIf { it >= 0 } ?: 0,
            pageCount = { images.size })

    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model = images[it].imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun ProductDetailLayout(product: Product) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(7.dp))
            .background(Background)
            .padding(16.dp)
    ) {
        Text(
            StringFormat.formatPrice(product.salePrice ?: product.originalPrice),
            color = TextSalePrice,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        VerticalSpacer(10)

        Text(
            product.productName,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        VerticalSpacer(10)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                "Đã bán ${product.quantitySold}",
                fontWeight = FontWeight.W500,
                fontSize = 14.sp,
                color = TextPrimary
            )

            Row() {
                Text("Đánh giá:")
                HorizontalSpacer(10)
                RatingBar(rating = product.rating)
            }
        }

        VerticalSpacer(5)

        HorizontalDivider(
            color = Border,
            thickness = 1.dp,
        )

        VerticalSpacer(5)

        Text("Mô tả sản phẩm", fontWeight = FontWeight.Bold, fontSize = 25.sp)

        TextHasSpan(
            label = "Loại sản phẩm: ",
            product.category.categoryName,
            SpanStyle(fontWeight = FontWeight.W500),
            SpanStyle(fontWeight = FontWeight.Normal)
        )
        Text(product.description)
    }
}

@Composable
fun ProductPriceLayout(originalPrice: Long, salePrice: Long, stock: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    bottomStart = 7.dp,
                    bottomEnd = 7.dp,
                )
            )
            .background(Background)
            .padding(16.dp)
    ) {
        if (stock > 0) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.check_24),
                    contentDescription = null,
                    tint = Active
                )
                Text(
                    "Còn hàng",
                    color = Active,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W600
                )
            }
        } else {
            Text(
                "Đã hết hàng",
                color = Inactive,
                fontSize = 15.sp,
                fontWeight = FontWeight.W600
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Giá niêm yết:")
            Text(
                StringFormat.formatPrice(originalPrice), color = TextOriginalPrice,
                fontSize = 18.sp,
                textDecoration = TextDecoration.LineThrough,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Giá khuyến mại:")
            Text(
                StringFormat.formatPrice(salePrice), color = TextSalePrice,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SpecificationsLayout(specifications: List<Specification>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(7.dp))
            .background(Background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("THÔNG SỐ KỸ THUẬT", fontWeight = FontWeight.Bold, fontSize = 25.sp)
        specifications.forEachIndexed { index, specification ->
            val backgroundCard = if (index % 2 == 0) Background else Color(0xfff2f3f7)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundCard)
                    .padding(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "${specification.keyName}:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = specification.value,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(2f),
                    softWrap = true
                )
            }

        }
    }
}

@Composable
private fun HandleProductSideEffects(
    state: ProductUiState,
    viewModel: ProductViewModel,
) {
    when (val status = state.status) {
        is ProductState.Failure -> MessageDialog(message = status.message, {
            viewModel.resetState()
        })

        is ProductState.Loading -> LoadingDialog(message = status.message)
        ProductState.None -> Unit
        is ProductState.Success -> MessageDialog(message = status.message, {
            viewModel.resetState()
        })
    }
}