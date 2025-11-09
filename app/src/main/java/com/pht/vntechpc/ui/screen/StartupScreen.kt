package com.pht.vntechpc.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.pht.vntechpc.R
import com.pht.vntechpc.ui.navigation.Route
import com.pht.vntechpc.ui.theme.DarkBackground
import com.pht.vntechpc.viewmodel.StartupViewModel

@Composable
fun StartupScreen(navController: NavController, viewModel: StartupViewModel = hiltViewModel()) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn == true) {
            navController.navigate(Route.Main.route) {
                popUpTo(Route.Startup.route) {
                    inclusive = true
                }
            }
        } else {
            navController.navigate(Route.Login.route) {
                popUpTo(Route.Startup.route) {
                    inclusive = true
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground).systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .size(120.dp)
                .clip(shape = CircleShape),
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "VNTech PC Shop",
            color = White,
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(100.dp))
    }
}