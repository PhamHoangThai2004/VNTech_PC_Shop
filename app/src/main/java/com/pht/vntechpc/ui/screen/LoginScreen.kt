package com.pht.vntechpc.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.pht.vntechpc.R
import com.pht.vntechpc.ui.component.FilledButtonComponent
import com.pht.vntechpc.ui.component.LoadingDialog
import com.pht.vntechpc.ui.component.MessageDialog
import com.pht.vntechpc.ui.component.OutlinedButtonComponent
import com.pht.vntechpc.ui.component.textFieldColors
import com.pht.vntechpc.ui.navigation.Route
import com.pht.vntechpc.ui.theme.Background
import com.pht.vntechpc.ui.theme.Border
import com.pht.vntechpc.ui.theme.TextDisabled
import com.pht.vntechpc.ui.theme.TextPrimary
import com.pht.vntechpc.viewmodel.LoginState
import com.pht.vntechpc.viewmodel.LoginUiState
import com.pht.vntechpc.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    HandleLoginSideEffects(state, viewModel, navController)
    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Đăng nhập", fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background,
                    titleContentColor = TextPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
            Image(
                modifier = Modifier
                    .size(80.dp)
                    .clip(shape = CircleShape),
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = modifier,
                value = state.email,
                onValueChange = { viewModel.updateEmail(it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = !state.isValidEmail,
                supportingText = { if (!state.isValidEmail) Text(text = state.emailError) },
                shape = RoundedCornerShape(6.dp),
                leadingIcon = {
                    Icon(
                        painterResource(R.drawable.email_24),
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    if (state.email.isNotEmpty())
                        IconButton(onClick = { viewModel.updateEmail("") }) {
                            Icon(
                                painterResource(R.drawable.close_24),
                                contentDescription = null
                            )
                        }
                    else null
                },
                singleLine = true,
                label = { Text(text = "Email") },
                colors = textFieldColors()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                modifier = modifier,
                value = state.password,
                onValueChange = { viewModel.updatePassword(it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = !state.isValidPassword,
                supportingText = { if (!state.isValidPassword) Text(text = state.passwordError) },
                shape = RoundedCornerShape(6.dp),
                leadingIcon = {
                    Icon(
                        painterResource(R.drawable.lock_24),
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                        Icon(
                            painter = painterResource(
                                id =
                                    if (state.isPasswordVisible) R.drawable.visibility_off_24
                                    else R.drawable.visibility_24
                            ),
                            contentDescription = "Mật khẩu"
                        )
                    }
                },
                singleLine = true,
                visualTransformation =
                    if (!state.isPasswordVisible) PasswordVisualTransformation()
                    else VisualTransformation.None,
                label = { Text(text = "Mật khẩu") },
                colors = textFieldColors()
            )

            Text(
                text = "Quên mật khẩu?",
                color = TextPrimary,
                fontWeight = FontWeight.W500,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable(onClick = {
                        navController.navigate(Route.Forget.route)
                    }, indication = null, interactionSource = null)
            )

            Spacer(modifier = Modifier.height(20.dp))

            FilledButtonComponent(
                onClick = { viewModel.login(state.email, state.password) },
                content = "Đăng nhập", modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            DividerComponent()

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedButtonComponent(
                onClick = { navController.navigate(Route.Signup.route) },
                content = "Tạo tài khoản mới",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
        }
    }
}

@Composable
private fun DividerComponent() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = Border
        )
        Text(
            text = "hoặc",
            modifier = Modifier.padding(horizontal = 8.dp),
            color = TextDisabled
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = Border
        )
    }
}

@Composable
private fun HandleLoginSideEffects(
    state: LoginUiState,
    viewModel: LoginViewModel,
    navController: NavController
) {
    LaunchedEffect(state.status) {
        when (state.status) {
            is LoginState.Success -> {
                navController.navigate(Route.Main.route) {
                    popUpTo(0) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }

            else -> Unit
        }
    }

    when (val status = state.status) {
        is LoginState.Failure -> {
            MessageDialog(message = status.message, onAction = { viewModel.resetState() })
        }

        is LoginState.Loading -> LoadingDialog("Đang đăng nhập...")
        else -> Unit
    }
}