package com.pht.vntechpc.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.pht.vntechpc.R
import com.pht.vntechpc.ui.component.FilledButtonComponent
import com.pht.vntechpc.ui.component.LoadingDialog
import com.pht.vntechpc.ui.component.MessageDialog
import com.pht.vntechpc.ui.component.textFieldColors
import com.pht.vntechpc.ui.theme.Background
import com.pht.vntechpc.ui.theme.DarkBackground
import com.pht.vntechpc.ui.theme.IconOnPrimary
import com.pht.vntechpc.ui.theme.TextOnPrimary
import com.pht.vntechpc.viewmodel.ChangePasswordState
import com.pht.vntechpc.viewmodel.ChangePasswordUiState
import com.pht.vntechpc.viewmodel.ChangePasswordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    navController: NavController,
    viewModel: ChangePasswordViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text(text = "Đổi mật khẩu", fontWeight = FontWeight.Medium) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = TextOnPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painterResource(R.drawable.arrow_back_24),
                            tint = IconOnPrimary,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            val modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))

            OutlinedTextField(
                modifier = modifier,
                value = state.oldPassword,
                onValueChange = { viewModel.updateOldPassword(it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = !state.isValidOldPassword,
                supportingText = { if (!state.isValidOldPassword) Text(text = state.oldPasswordError) },
                shape = RoundedCornerShape(6.dp),
                leadingIcon = {
                    Icon(
                        painterResource(R.drawable.lock_24),
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { viewModel.toggleOldPasswordVisibility() }) {
                        Icon(
                            painter = painterResource(
                                id =
                                    if (state.isOldPasswordVisible) R.drawable.visibility_off_24
                                    else R.drawable.visibility_24
                            ),
                            contentDescription = "Mật khẩu cũ"
                        )
                    }
                },
                singleLine = true,
                visualTransformation =
                    if (!state.isOldPasswordVisible) PasswordVisualTransformation()
                    else VisualTransformation.None,
                label = { Text(text = "Mật khẩu cũ") },
                colors = textFieldColors()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = modifier,
                value = state.newPassword,
                onValueChange = { viewModel.updateNewPassword(it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = !state.isValidNewPassword,
                supportingText = { if (!state.isValidNewPassword) Text(text = state.newPasswordError) },
                shape = RoundedCornerShape(6.dp),
                leadingIcon = {
                    Icon(
                        painterResource(R.drawable.lock_24),
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { viewModel.toggleNewPasswordVisibility() }) {
                        Icon(
                            painter = painterResource(
                                id =
                                    if (state.isNewPasswordVisible) R.drawable.visibility_off_24
                                    else R.drawable.visibility_24
                            ),
                            contentDescription = "Mật khẩu mới"
                        )
                    }
                },
                singleLine = true,
                visualTransformation =
                    if (!state.isNewPasswordVisible) PasswordVisualTransformation()
                    else VisualTransformation.None,
                label = { Text(text = "Mật khẩu mới") },
                colors = textFieldColors()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = modifier,
                value = state.confirmPassword,
                onValueChange = { viewModel.updateConfirmPassword(it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = !state.isValidConfirmPassword,
                supportingText = { if (!state.isValidConfirmPassword) Text(text = state.confirmPasswordError) },
                shape = RoundedCornerShape(6.dp),
                leadingIcon = {
                    Icon(
                        painterResource(R.drawable.lock_24),
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { viewModel.toggleConfirmPasswordVisibility() }) {
                        Icon(
                            painter = painterResource(
                                id =
                                    if (state.isConfirmPasswordVisible) R.drawable.visibility_off_24
                                    else R.drawable.visibility_24
                            ),
                            contentDescription = "Xác nhận mật khẩu"
                        )
                    }
                },
                singleLine = true,
                visualTransformation =
                    if (!state.isConfirmPasswordVisible) PasswordVisualTransformation()
                    else VisualTransformation.None,
                label = { Text(text = "Xác nhận mật khẩu") },
                colors = textFieldColors()
            )

            Spacer(modifier = Modifier.height(20.dp))

            FilledButtonComponent(onClick = {
                viewModel.changePassword(
                    state.oldPassword,
                    state.newPassword,
                    state.confirmPassword
                )
            }, content = "Thay đổi", modifier = Modifier.fillMaxWidth())
        }
    }

    HandleChangePasswordSideEffects(state, viewModel, navController)
}

@Composable
private fun HandleChangePasswordSideEffects(
    state: ChangePasswordUiState,
    viewModel: ChangePasswordViewModel,
    navController: NavController
) {
    when (val status = state.status) {
        is ChangePasswordState.Success -> {
            MessageDialog(message = status.message, onAction = {
                viewModel.resetState()
                navController.popBackStack()
            })
        }

        is ChangePasswordState.Failure -> {
            MessageDialog(message = status.message, onAction = { viewModel.resetState() })
        }

        is ChangePasswordState.Loading -> LoadingDialog(message = status.message)
        else -> Unit
    }
}