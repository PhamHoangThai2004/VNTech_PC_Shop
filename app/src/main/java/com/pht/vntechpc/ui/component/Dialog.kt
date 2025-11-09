package com.pht.vntechpc.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pht.vntechpc.ui.theme.Background
import com.pht.vntechpc.ui.theme.DialogBackground
import com.pht.vntechpc.ui.theme.TextOnPrimary
import com.pht.vntechpc.ui.theme.TextPrimary

@Composable
fun LoadingDialog(message: String = "Đang xử lý...") {
    AlertDialog(
        onDismissRequest = { },
        confirmButton = {},
        containerColor = DialogBackground,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = Background)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = message, color = TextOnPrimary)
            }
        }
    )
}

@Composable
fun MessageDialog(message: String, onAction: () -> Unit) {
    AlertDialog(
        modifier = Modifier.padding(20.dp),
        shape = RoundedCornerShape(8.dp),
        containerColor = Background,
        onDismissRequest = { onAction },

        title = {
            Text(
                text = "Thông báo",
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = { Text(text = message, color = TextPrimary, fontSize = 16.sp) },
        confirmButton = { FilledButtonComponent(onClick = onAction, content = "OK") }
    )
}