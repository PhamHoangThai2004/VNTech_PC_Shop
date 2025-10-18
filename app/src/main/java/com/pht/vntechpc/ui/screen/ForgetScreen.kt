package com.pht.vntechpc.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
import com.pht.vntechpc.ui.component.OutlinedButtonComponent
import com.pht.vntechpc.ui.component.textFieldColors
import com.pht.vntechpc.ui.theme.Black
import com.pht.vntechpc.ui.theme.Gray
import com.pht.vntechpc.ui.theme.White
import com.pht.vntechpc.viewmodel.ForgetState
import com.pht.vntechpc.viewmodel.ForgetStatePage
import com.pht.vntechpc.viewmodel.ForgetUiState
import com.pht.vntechpc.viewmodel.ForgetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ForgetScreen(navController: NavController, viewModel: ForgetViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    HandleForgotSideEffects(state, viewModel, navController)
    Scaffold(
        containerColor = White,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Khôi phục mật khẩu")
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White, titleContentColor = Black
                ), navigationIcon = {
                    IconButton(onClick = {
                        when (state.isPage) {
                            ForgetStatePage.EnterEmail -> navController.popBackStack()
                            ForgetStatePage.ConfirmOtp -> viewModel.backToEnterEmail()
                            ForgetStatePage.EnterNewPassword -> viewModel.goToConfirmOtp()
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back_24),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                })
        }) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(90.dp))

            Image(
                modifier = Modifier
                    .size(80.dp)
                    .clip(shape = CircleShape),
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (state.isPage) {
                ForgetStatePage.EnterEmail -> EnterEmailPage(viewModel, state)
                ForgetStatePage.ConfirmOtp -> ConfirmOtpPage(viewModel, state)
                ForgetStatePage.EnterNewPassword -> EnterNewPasswordPage(viewModel, state)
            }
        }
    }
}

@Composable
private fun EnterEmailPage(viewModel: ForgetViewModel, state: ForgetUiState) {
    Column {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.email,
            onValueChange = { viewModel.updateEmail(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = !state.isValidEmail,
            supportingText = { if (!state.isValidEmail) Text(text = state.emailError) },
            shape = RoundedCornerShape(6.dp),
            leadingIcon = {
                Icon(
                    painterResource(R.drawable.email_24), contentDescription = null
                )
            },
            trailingIcon = {
                if (state.email.isNotEmpty()) IconButton(onClick = {
                    viewModel.updateEmail("")
                }) { Icon(painterResource(R.drawable.close_24), contentDescription = null) }
                else null
            },
            singleLine = true,
            label = { Text(text = "Email") },
            colors = textFieldColors()
        )

        Spacer(modifier = Modifier.height(20.dp))

        FilledButtonComponent(
            onClick = { viewModel.forgotPassword(state.email) },
            content = "Gửi mã qua Email",
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
    }
}

@Composable
private fun ConfirmOtpPage(viewModel: ForgetViewModel, state: ForgetUiState) {
    Column {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.otp,
            onValueChange = { viewModel.updateOtp(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = !state.isValidOtp,
            supportingText = { if (!state.isValidOtp) Text(text = state.otpError) },
            shape = RoundedCornerShape(6.dp),
            singleLine = true,
            placeholder = { Text(text = "Nhập mã OTP", color = Gray) },
            colors = textFieldColors()
        )

        Spacer(modifier = Modifier.height(20.dp))

        FilledButtonComponent(
            onClick = { viewModel.verifyResetOtp(state.otp) },
            content = "Xác nhận",
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButtonComponent(
            onClick = { viewModel.resendOtp() },
            content = "Gửi lại mã",
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
    }
}

@Composable
private fun EnterNewPasswordPage(viewModel: ForgetViewModel, state: ForgetUiState) {
    Column {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.newPassword,
            onValueChange = { viewModel.updatePassword(it) },
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
                        contentDescription = "Mật khẩu"
                    )
                }
            },
            singleLine = true,
            visualTransformation =
                if (!state.isNewPasswordVisible) PasswordVisualTransformation()
                else VisualTransformation.None,
            label = { Text(text = "Mật khẩu") },
            colors = textFieldColors()
        )

        Spacer(modifier = Modifier.height(20.dp))

        FilledButtonComponent(
            onClick = { viewModel.resetPassword(state.email, state.newPassword) },
            content = "Khôi phục mật khẩu",
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
    }
}

@Composable
private fun HandleForgotSideEffects(
    state: ForgetUiState,
    viewModel: ForgetViewModel,
    navController: NavController
) {
    when (val status = state.status) {
        is ForgetState.ForgetPasswordSuccess -> {
            MessageDialog(message = status.message, onAction = {
                viewModel.resetState()
                viewModel.goToConfirmOtp()
            })
        }

        is ForgetState.VerifyResetOtpSuccess -> {
            MessageDialog(message = status.message, onAction = {
                viewModel.resetState()
                viewModel.goToEnterNewPassword()
            })
        }

        is ForgetState.ResetPasswordSuccess -> {
            MessageDialog(message = status.message, onAction = {
                viewModel.resetState()
                navController.popBackStack()
            })
        }

        is ForgetState.Failure -> {
            MessageDialog(message = status.message, onAction = { viewModel.resetState() })
        }

        is ForgetState.Loading -> LoadingDialog()
        else -> Unit
    }
}