package com.pht.vntechpc.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pht.vntechpc.domain.model.PaymentMethod
import com.pht.vntechpc.ui.theme.Checked
import com.pht.vntechpc.ui.theme.UncheckBorder

@Composable
fun PaymentMethodItemCard(
    paymentMethod: PaymentMethod,
    isSelected: Boolean,
    onClick: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(paymentMethod.name)
        RadioButton(
            onClick = { onClick(paymentMethod.code) },
            selected = isSelected,
            colors = RadioButtonDefaults.colors(
                selectedColor = Checked,
                unselectedColor = UncheckBorder
            )
        )
    }
}