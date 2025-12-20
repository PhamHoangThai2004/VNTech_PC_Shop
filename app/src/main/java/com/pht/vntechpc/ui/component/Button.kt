package com.pht.vntechpc.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pht.vntechpc.ui.theme.ButtonPrimaryBackground
import com.pht.vntechpc.ui.theme.ButtonPrimaryContent
import com.pht.vntechpc.ui.theme.ButtonSecondaryBackground
import com.pht.vntechpc.ui.theme.ButtonSecondaryBorder
import com.pht.vntechpc.ui.theme.ButtonSecondaryContent

@Composable
fun FilledButtonComponent(
    onClick: () -> Unit,
    content: String,
    modifier: Modifier = Modifier,
    enable: Boolean = true
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enable,
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = ButtonPrimaryBackground,
            contentColor = ButtonPrimaryContent
        )
    ) {
        Text(text = content)
    }
}

@Composable
fun OutlinedButtonComponent(
    onClick: () -> Unit,
    content: String,
    modifier: Modifier = Modifier,
    icon: Int? = null
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, ButtonSecondaryBorder),
        colors = ButtonDefaults.buttonColors(
            containerColor = ButtonSecondaryBackground,
            contentColor = ButtonSecondaryContent
        )
    ) {
        if (icon != null) {
            Icon(painter = painterResource(id = icon), contentDescription = null)
            HorizontalSpacer(10)
        }
        Text(text = content)
    }
}