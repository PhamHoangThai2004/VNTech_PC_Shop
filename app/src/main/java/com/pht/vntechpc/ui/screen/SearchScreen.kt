package com.pht.vntechpc.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.pht.vntechpc.R
import com.pht.vntechpc.ui.theme.Background
import com.pht.vntechpc.ui.theme.BackgroundSecondary
import com.pht.vntechpc.ui.theme.CursorColor
import com.pht.vntechpc.ui.theme.DarkBackground
import com.pht.vntechpc.ui.theme.IconPrimary
import com.pht.vntechpc.ui.theme.TextFieldPlaceholder
import com.pht.vntechpc.ui.theme.TextOnPrimary
import com.pht.vntechpc.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(viewModel: SearchViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Background,
        topBar = {
            when (state.page) {
                SearchStatePage.SearchMainPage -> SearchAppBarDefault()
                SearchStatePage.SearchInputPage -> SearchBox(state, viewModel)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            when (state.page) {
                SearchStatePage.SearchMainPage -> SearchMainPage(viewModel)
                SearchStatePage.SearchInputPage -> SearchInputPage()
            }
        }
    }
}

@Composable
fun SearchMainPage(viewModel: SearchViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(BackgroundSecondary)
            .padding(horizontal = 16.dp)
            .clickable (
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                viewModel.goToSearchInputPage()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.search_24),
            tint = IconPrimary,
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "Tìm kiếm sản phẩm",
            color = TextFieldPlaceholder,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun SearchInputPage() {
    Column() {
        Text(text = "Search Input Screen")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBarDefault() {
    TopAppBar(
        title = { Text(text = "Tìm kiếm", fontWeight = FontWeight.Medium) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = DarkBackground,
            titleContentColor = TextOnPrimary
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBox(state: SearchUiState, viewModel: SearchViewModel = hiltViewModel()) {
    TopAppBar(
        title = {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(30.dp)),
                value = state.keyword,
                onValueChange = { viewModel.updateKeyword(it) },
                singleLine = true,
                leadingIcon = {
                    Icon(painter = painterResource(id = R.drawable.search_24), contentDescription = null)
                },
                trailingIcon = {
                    if (state.keyword.isNotEmpty())
                        IconButton(onClick = { viewModel.updateKeyword("") }) {
                            Icon(painterResource(R.drawable.close_24), contentDescription = null)
                        }
                    else null
                },
                textStyle = TextStyle(
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                ),
                placeholder = { Text(text = "Nhập tên sản phẩm", color = TextFieldPlaceholder) },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = CursorColor,
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = { viewModel.backToSearchMainPage() }) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_back_24),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    )
}