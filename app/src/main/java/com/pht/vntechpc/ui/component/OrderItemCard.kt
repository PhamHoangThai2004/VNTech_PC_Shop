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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pht.vntechpc.domain.model.OrderShort
import com.pht.vntechpc.ui.theme.CardBackground
import com.pht.vntechpc.ui.theme.TextPrimary
import com.pht.vntechpc.ui.theme.TextSalePrice
import com.pht.vntechpc.utils.EnumOrderStatus
import com.pht.vntechpc.utils.StringFormat

@Composable
fun OrderItemCard(orderShort: OrderShort, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clip(RoundedCornerShape(7.dp))
            .background(CardBackground)
            .padding(12.dp)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() })
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "#${orderShort.orderCode}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                TextHasSpan(
                    "Trạng thái: ",
                    EnumOrderStatus.toStatus(orderShort.status),
                    SpanStyle(color = TextPrimary),
                    SpanStyle(
                        color = EnumOrderStatus.toColor(orderShort.status),
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            VerticalSpacer(8)

            Text(text = "Ngày đặt hàng: ${StringFormat.formatTime(orderShort.createdAt)}")

            VerticalSpacer(8)

            TextHasSpan(
                "Tên người nhận: ",
                orderShort.recipientName,
                SpanStyle(color = TextPrimary),
                SpanStyle(color = TextPrimary, fontWeight = FontWeight.W500)
            )

            VerticalSpacer(8)

            TextHasSpan(
                "Số điện thoại: ",
                orderShort.phoneNumber,
                SpanStyle(color = TextPrimary),
                SpanStyle(color = TextPrimary, fontWeight = FontWeight.W500)
            )

            VerticalSpacer(8)

            TextHasSpan(
                "Địa chỉ nhận hàng: ", orderShort.shortAddress,
                SpanStyle(color = TextPrimary),
                SpanStyle(color = TextPrimary, fontWeight = FontWeight.W500)
            )

            TextHasSpan(
                "Số sản phẩm: ",
                orderShort.itemCount.toString(),
                SpanStyle(color = TextPrimary),
                SpanStyle(color = TextPrimary, fontWeight = FontWeight.W500)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                TextHasSpan(
                    "Tổng tiền: ",
                    StringFormat.formatPrice(orderShort.finalPrice),
                    SpanStyle(color = TextPrimary, fontSize = 20.sp),
                    SpanStyle(color = TextSalePrice, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )

                OutlinedButtonComponent(onClick = {}, content = "Mua lại")
            }
        }
    }
}