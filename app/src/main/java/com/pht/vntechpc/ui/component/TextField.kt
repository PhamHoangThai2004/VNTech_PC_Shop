package com.pht.vntechpc.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import com.pht.vntechpc.ui.theme.CursorColor
import com.pht.vntechpc.ui.theme.ErrorTextField
import com.pht.vntechpc.ui.theme.FocusedTextFieldBorder
import com.pht.vntechpc.ui.theme.IconSecondary
import com.pht.vntechpc.ui.theme.TextFieldBackground
import com.pht.vntechpc.ui.theme.TextFieldContent
import com.pht.vntechpc.ui.theme.TextFieldIcon
import com.pht.vntechpc.ui.theme.TextFieldLabel
import com.pht.vntechpc.ui.theme.UnfocusedTextFieldBorder

@Composable
fun UnderlinedTextField(
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.fillMaxWidth(),
    value: String,
    onValueChange: (String) -> Unit = {},
    label: String,
    isError: Boolean = false,
    supportingText: String = "",
    readOnly: Boolean = false,
    enabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingIcon: Int? = null,
    textFieldColors: TextFieldColors = textFieldColors()
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        isError = isError,
        readOnly = readOnly,
        enabled = enabled,
        trailingIcon = {
            if (trailingIcon != null)
                Icon(
                    painter = painterResource(trailingIcon),
                    contentDescription = null,
                )
        },
        supportingText = { if (isError) Text(text = supportingText) },
        colors = textFieldColors
    )
}

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

@Composable
fun textFieldDisableColors() = TextFieldDefaults.colors(
    disabledTextColor = TextFieldContent,
    disabledLabelColor = TextFieldContent,
    disabledContainerColor = TextFieldBackground,
    disabledTrailingIconColor = TextFieldIcon
)