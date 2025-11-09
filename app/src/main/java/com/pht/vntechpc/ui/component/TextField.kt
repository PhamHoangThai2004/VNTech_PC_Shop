package com.pht.vntechpc.ui.component

import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import com.pht.vntechpc.ui.theme.CursorColor
import com.pht.vntechpc.ui.theme.ErrorTextField
import com.pht.vntechpc.ui.theme.FocusedTextFieldBorder
import com.pht.vntechpc.ui.theme.IconSecondary
import com.pht.vntechpc.ui.theme.TextFieldBackground
import com.pht.vntechpc.ui.theme.TextFieldContent
import com.pht.vntechpc.ui.theme.TextFieldLabel
import com.pht.vntechpc.ui.theme.UnfocusedTextFieldBorder

@Composable
fun textFieldColors() = TextFieldDefaults.colors(
    focusedLabelColor = TextFieldLabel,
    focusedTextColor = TextFieldContent,
    unfocusedTextColor = TextFieldContent,
    focusedContainerColor = TextFieldBackground,
    unfocusedContainerColor = TextFieldBackground,
    focusedIndicatorColor = FocusedTextFieldBorder,
    unfocusedIndicatorColor = UnfocusedTextFieldBorder,
    errorSupportingTextColor = ErrorTextField,
    errorIndicatorColor = ErrorTextField,
    errorContainerColor = TextFieldBackground,
    errorLabelColor = ErrorTextField,
    errorCursorColor = ErrorTextField,
    errorTrailingIconColor = IconSecondary,
    cursorColor = CursorColor
)