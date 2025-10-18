package com.pht.vntechpc.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pht.vntechpc.ui.theme.Transparent
import com.pht.vntechpc.ui.theme.White

@Composable
fun LoadingDialog() {
    AlertDialog(
        onDismissRequest = { },
        confirmButton = {},
        containerColor = Transparent,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = White)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Đang xử lý...", color = White)
            }
        }
    )
}

@Composable
fun MessageDialog(message: String, onAction: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onAction },
        title = { Text(text = "Thông báo") },
        text = { Text(text = message) },
        confirmButton = {
            Button(onClick = onAction) {
                Text(text = "OK")
            }
        }
    )
}