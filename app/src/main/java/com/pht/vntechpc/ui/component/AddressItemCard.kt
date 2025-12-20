package com.pht.vntechpc.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pht.vntechpc.domain.model.Address
import com.pht.vntechpc.ui.theme.CardBackground
import com.pht.vntechpc.ui.theme.Checked
import com.pht.vntechpc.ui.theme.DarkBackground
import com.pht.vntechpc.ui.theme.TextOnPrimary
import com.pht.vntechpc.ui.theme.TextPrimary
import com.pht.vntechpc.ui.theme.UncheckBorder
import com.pht.vntechpc.utils.StringFormat

@Composable
fun AddressItemCard(address: Address, onClick: (Address) -> Unit, onSetDefault: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(address) }
            .padding(bottom = 10.dp)
            .clip(RoundedCornerShape(7.dp))
            .background(CardBackground)
            .padding(top = 24.dp, end = 24.dp, bottom = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                onClick = { onSetDefault(address.id) },
                selected = address.isDefault,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Checked,
                    unselectedColor = UncheckBorder
                )
            )
            HorizontalSpacer(10)
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = address.recipientName,
                        fontSize = 18.sp,
                        color = TextPrimary,
                        fontWeight = FontWeight.W500
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    if (address.isDefault) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(DarkBackground)
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Mặc định", color = TextOnPrimary)
                        }
                    }

                }
                VerticalSpacer(10)
                Text(text = address.addressDetail, color = TextPrimary, fontSize = 16.sp)
                VerticalSpacer(10)
                Text(
                    text = "${address.ward}, ${address.district}, ${address.province}",
                    color = TextPrimary,
                    fontSize = 16.sp
                )
                VerticalSpacer(10)
                Text(
                    text = StringFormat.formatPhoneNumber(address.phoneNumber),
                    color = TextPrimary,
                    fontSize = 16.sp
                )
            }
        }
    }
}