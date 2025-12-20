package com.pht.vntechpc.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pht.vntechpc.ui.theme.IconStar
import com.pht.vntechpc.ui.theme.TextPrimary

@Composable
fun RatingBar(rating: Float) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        for (i in 0 until 5) {
            val icon = when {
                rating >= i + 1 -> Icons.Default.Star

                rating > i -> Icons.AutoMirrored.Filled.StarHalf

                else -> Icons.Default.StarBorder
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = IconStar,
                modifier = Modifier.size(15.dp)
            )
        }
        HorizontalSpacer(10)
        Text("(${rating})", fontSize = 13.sp, color = TextPrimary, fontWeight = FontWeight.W400)
    }
}
