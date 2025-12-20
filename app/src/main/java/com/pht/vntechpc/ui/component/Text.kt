package com.pht.vntechpc.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@Composable
fun TextHasSpan(label: String, content: String, spanStyle1: SpanStyle, spanStyle2: SpanStyle) {
    Text(text = buildAnnotatedString {
        withStyle(
            style = spanStyle1
        ) {
            append(label)
        }
        withStyle(
            style = spanStyle2
        ) {
            append(content)
        }
    })
}