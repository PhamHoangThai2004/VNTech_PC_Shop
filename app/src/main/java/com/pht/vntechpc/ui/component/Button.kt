package com.pht.vntechpc.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pht.vntechpc.ui.theme.Black
import com.pht.vntechpc.ui.theme.White

@Composable
fun FilledButtonComponent(onClick: () -> Unit, content: String, modifier: Modifier = Modifier) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Black,
            contentColor = White
        )
    ) {
        Text(text = content)
    }
}

@Composable
fun OutlinedButtonComponent(onClick: () -> Unit, content: String, modifier: Modifier = Modifier) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, Black),
        colors = ButtonDefaults.buttonColors(
            containerColor = White,
            contentColor = Black
        )
    ) {
        Text(text = content)
    }
}

@Preview
@Composable
fun ButtonPreview() {
    OutlinedButtonComponent(onClick = {}, content = "Tạo tài khoản mới")
}