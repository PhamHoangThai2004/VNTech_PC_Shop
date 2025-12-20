package com.pht.vntechpc.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.pht.vntechpc.R
import com.pht.vntechpc.domain.model.Address
import com.pht.vntechpc.ui.component.AddressItemCard
import com.pht.vntechpc.ui.component.LoadingDialog
import com.pht.vntechpc.ui.component.MessageDialog
import com.pht.vntechpc.ui.navigation.Route
import com.pht.vntechpc.ui.theme.Background
import com.pht.vntechpc.ui.theme.DarkBackground
import com.pht.vntechpc.ui.theme.IconOnPrimary
import com.pht.vntechpc.ui.theme.TextOnPrimary
import com.pht.vntechpc.utils.EnumConst
import com.pht.vntechpc.viewmodel.AddressState
import com.pht.vntechpc.viewmodel.AddressUiState
import com.pht.vntechpc.viewmodel.AddressViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(navController: NavController, viewModel: AddressViewModel = hiltViewModel()) {
    val state = viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getAddresses()
    }

    HandleAddressSideEffects(state.value, viewModel)

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text(text = "Địa chỉ", fontWeight = FontWeight.Medium) },
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
                    IconButton(onClick = { navController.navigate(Route.AddressForm.route) }) {
                        Icon(
                            painter = painterResource(R.drawable.add_24),
                            contentDescription = null,
                            tint = IconOnPrimary
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!state.value.addresses.isEmpty()) {
                AddressesList(
                    addresses = state.value.addresses,
                    navController = navController,
                    viewModel::setDefaultAddress
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Không có địa chỉ nào")
                }
            }
        }
    }
}

@Composable
private fun AddressesList(
    addresses: List<Address>,
    navController: NavController,
    onSetDefaultAddress: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(addresses.size) { index ->
            AddressItemCard(address = addresses[index], onClick = {
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    EnumConst.ADDRESS_KEY,
                    it
                )
                navController.navigate(Route.AddressForm.route)
            }, onSetDefault = {
                onSetDefaultAddress(it)
            })
        }
    }
}

@Composable
private fun HandleAddressSideEffects(state: AddressUiState, viewModel: AddressViewModel) {
    when (val status = state.status) {
        is AddressState.Failure -> MessageDialog(
            message = status.message,
            onAction = { viewModel.resetState() })

        is AddressState.Loading -> LoadingDialog(message = status.message)

        AddressState.None -> Unit
    }
}