package com.pht.vntechpc.ui.component

import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import com.pht.vntechpc.ui.theme.Alto
import com.pht.vntechpc.ui.theme.Black
import com.pht.vntechpc.ui.theme.Red
import com.pht.vntechpc.ui.theme.White

@Composable
fun textFieldColors() = TextFieldDefaults.colors(
    focusedLabelColor = Black,
    focusedTextColor = Black,
    unfocusedTextColor = Black,
    focusedContainerColor = White,
    unfocusedContainerColor = White,
    focusedIndicatorColor = Black,
    unfocusedIndicatorColor = Alto,
    errorSupportingTextColor = Red,
    errorIndicatorColor = Red,
    errorContainerColor = White,
    errorLabelColor = Red,
    errorCursorColor = Red,
    errorTrailingIconColor = DarkGray,
    cursorColor = Black
)