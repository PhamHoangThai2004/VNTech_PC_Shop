@file:Suppress(
    "INFERRED_TYPE_VARIABLE_INTO_EMPTY_INTERSECTION_WARNING",
    "TYPE_INTERSECTION_AS_REIFIED_WARNING"
)

package com.pht.vntechpc.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.pht.vntechpc.R
import com.pht.vntechpc.ui.navigation.Route
import com.pht.vntechpc.ui.theme.Background
import com.pht.vntechpc.ui.theme.Border
import com.pht.vntechpc.ui.theme.DarkBackground
import com.pht.vntechpc.ui.theme.IconOnPrimary
import com.pht.vntechpc.ui.theme.TextOnPrimary
import com.pht.vntechpc.utils.EnumConst
import com.pht.vntechpc.viewmodel.AccountViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(navController: NavController, viewModel: AccountViewModel = hiltViewModel()) {
    val user by viewModel.user.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getUser()
    }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text(text = "Tài khoản", fontWeight = FontWeight.Medium) },
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
                },
                actions = {
                    TextButton(onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            EnumConst.USER_KEY,
                            user
                        )
                        navController.navigate(Route.EditProfile.route)
                    }) {
                        Text(text = "Chỉnh sửa", color = IconOnPrimary)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(16.dp),
        ) {

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(2.dp, Border, CircleShape),
                ) {
                    AsyncImage(
                        model = user?.avatar,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        placeholder = painterResource(R.drawable.default_avatar),
                        error = painterResource(R.drawable.default_avatar)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            BoxComponent("Email", user?.email)

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Border
            )

            BoxComponent("Họ và tên", user?.fullName)

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Border
            )
            BoxComponent("Ngày sinh", user?.dateOfBirth)

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Border
            )

            BoxComponent("Giới tính", user?.gender)
        }

    }
}

@Composable
private fun BoxComponent(label: String, content: String?) {
    Column {
        Text(text = label)
        Text(text = content ?: "Chưa có")
    }
}

@Preview(showBackground = true)
@Composable
fun AccountPreview() {
    AccountScreen(navController = rememberNavController())
}