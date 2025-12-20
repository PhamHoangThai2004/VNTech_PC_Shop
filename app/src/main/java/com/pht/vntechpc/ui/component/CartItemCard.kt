package com.pht.vntechpc.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pht.vntechpc.R
import com.pht.vntechpc.domain.model.CartItem
import com.pht.vntechpc.ui.theme.CardBackground
import com.pht.vntechpc.ui.theme.CheckMark
import com.pht.vntechpc.ui.theme.Checked
import com.pht.vntechpc.ui.theme.TextSalePrice
import com.pht.vntechpc.ui.theme.Unchecked
import com.pht.vntechpc.utils.StringFormat

@Composable
fun CartItemCard(
    cartItem: CartItem,
    onQuantityChange: (Int) -> Unit,
    onRemoveItem: () -> Unit,
    onToggleSelected: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clip(RoundedCornerShape(7.dp))
            .background(CardBackground)
            .padding(12.dp),
    ) {
        Row {

            Checkbox(
                checked = cartItem.selected,
                onCheckedChange = { onToggleSelected(it) },
                modifier = Modifier.align(Alignment.CenterVertically),
                colors = CheckboxDefaults.colors(
                    checkedColor = Checked,
                    uncheckedColor = Unchecked,
                    checkmarkColor = CheckMark
                )
            )

            HorizontalSpacer(4)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
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
                IconButton(onClick = { onRemoveItem() }) {
                    Icon(
                        painter = painterResource(R.drawable.delete_24),
                        contentDescription = null
                    )
                }
            }

            HorizontalSpacer(8)

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    cartItem.product.productName,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )

                Text("#${cartItem.product.model}", fontSize = 12.sp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    QuantitySelector(value = cartItem.quantity, onValueChange = {
                        onQuantityChange(it)
                    })

                    Text(
                        text = StringFormat.formatPrice(cartItem.price),
                        color = TextSalePrice,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun QuantitySelector(value: Int, onValueChange: (Int) -> Unit) {
    var input by remember { mutableStateOf(value.toString()) }

    LaunchedEffect(value) {
        input = value.toString()
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {

        IconButton(
            enabled = value > 1,
            onClick = {
                onValueChange(value - 1)
            }) {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(R.drawable.remove_24), contentDescription = null
            )
        }

        Box(
            modifier = Modifier
                .size(36.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = input,
                onValueChange = {
                    input = it
                    val intValue = it.toIntOrNull()
                    if (intValue != null && intValue in 1..99) {
                        onValueChange(intValue)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        IconButton(
            enabled = value < 99,
            onClick = {
                onValueChange(value + 1)
            }) {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(R.drawable.add_24), contentDescription = null
            )
        }
    }
}

//@Preview
//@Composable
//fun CartItemPreview() {
//    val p = ProductInCart(
//        id = 1,
//        productName = "Laptop",
//        salePrice = 900000,
//        brand = "Dell",
//        model = "Dell",
//        stock = 10,
//        mainImage = null,
//    )
//    val c = CartItem(
//        id = 1,
//        quantity = 1,
//        price = 1000000,
//        selected = true,
//        product = p
//    )
//    CartItemCard(c, onQuantityChange = {}, onRemoveItem = {}, onToggleSelected = {})
//}