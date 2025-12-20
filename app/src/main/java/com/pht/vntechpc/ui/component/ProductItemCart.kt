package com.pht.vntechpc.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pht.vntechpc.R
import com.pht.vntechpc.domain.model.Category
import com.pht.vntechpc.domain.model.Product
import com.pht.vntechpc.ui.theme.Active
import com.pht.vntechpc.ui.theme.CardBackground
import com.pht.vntechpc.ui.theme.Inactive
import com.pht.vntechpc.ui.theme.Info
import com.pht.vntechpc.ui.theme.TextOriginalPrice
import com.pht.vntechpc.ui.theme.TextPrimary
import com.pht.vntechpc.ui.theme.TextSalePrice
import com.pht.vntechpc.utils.StringFormat

@Composable
fun ProductItemCart(product: Product, onAddToCart: (Int) -> Unit, onProductClick: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    onProductClick(product.id)
                }
            )
            .padding(bottom = 10.dp)
            .clip(RoundedCornerShape(7.dp))
            .background(CardBackground)
            .padding(12.dp)) {
        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
            ) {
                val imageUrl = product.images?.firstOrNull { it.main }?.imageUrl
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    placeholder = painterResource(R.drawable.app_logo),
                    error = painterResource(R.drawable.app_logo)
                )
            }

            VerticalSpacer(8)

            Text(
                text = product.productName,
                fontSize = 16.sp,
                color = TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            RatingBar(rating = product.rating)

            if (product.salePrice != product.originalPrice && product.originalPrice > 0) {
                Text(
                    text = StringFormat.formatPrice(product.originalPrice),
                    fontSize = 15.sp,
                    color = TextOriginalPrice,
                    textDecoration = TextDecoration.LineThrough,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
            Text(
                text = StringFormat.formatPrice(product.salePrice ?: product.originalPrice),
                fontSize = 16.sp,
                color = TextSalePrice,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                if (product.stock > 0) {
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
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W600
                        )
                    }
                } else {
                    Text(
                        "Đã hết hàng",
                        color = Inactive,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W600
                    )
                }

                IconButton(onClick = { onAddToCart(product.id) }) {
                    Icon(
                        painter = painterResource(R.drawable.add_shopping_cart_24),
                        contentDescription = null,
                        tint = Info
                    )
                }
            }
        }

    }
}