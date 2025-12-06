package com.pht.vntechpc.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import com.pht.vntechpc.utils.formatPrice

@Composable
fun CartItemCard(
    cartItem: CartItem,
    onQuantityChange: (Int) -> Unit,
    onRemoveItem: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = cartItem.product.productName,
                maxLines = 2,
                fontSize = 10.sp,
            )

            // Model
            Text(
                text = cartItem.product.model,
                fontSize = 12.sp,
                color = Color.Cyan,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            AsyncImage(
                model = cartItem.product.mainImage,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                placeholder = painterResource(R.drawable.ic_launcher_background),
                error = painterResource(R.drawable.ic_launcher_background)
            )
        }


        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End
        ) {
            // Price
            Text(
                text = "Giá: ${formatPrice(cartItem.price)}",
                color = Color.Red,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            IconButton(onClick = { onRemoveItem() }) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(R.drawable.delete_24), contentDescription = null
                )
            }

            QuantitySelector(value = cartItem.quantity, onValueChange = {
                onQuantityChange(it)
            })


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
        horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                fontSize = 16.sp
            ),
            singleLine = true,
            modifier = Modifier
                .size(40.dp)
                .border(1.dp, Color.Red, RoundedCornerShape(6.dp))
                .padding(vertical = 8.dp)
        )

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