package com.pht.vntechpc.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.pht.vntechpc.R
import com.pht.vntechpc.domain.model.Address
import com.pht.vntechpc.ui.component.ConfirmDialog
import com.pht.vntechpc.ui.component.FilledButtonComponent
import com.pht.vntechpc.ui.component.LoadingDialog
import com.pht.vntechpc.ui.component.MessageDialog
import com.pht.vntechpc.ui.component.OutlinedButtonComponent
import com.pht.vntechpc.ui.component.UnderlinedTextField
import com.pht.vntechpc.ui.component.VerticalSpacer
import com.pht.vntechpc.ui.component.textFieldDisableColors
import com.pht.vntechpc.ui.theme.Background
import com.pht.vntechpc.ui.theme.CheckThumb
import com.pht.vntechpc.ui.theme.CheckTrack
import com.pht.vntechpc.ui.theme.DarkBackground
import com.pht.vntechpc.ui.theme.IconOnPrimary
import com.pht.vntechpc.ui.theme.TextOnPrimary
import com.pht.vntechpc.ui.theme.UncheckBorder
import com.pht.vntechpc.ui.theme.UncheckedThumb
import com.pht.vntechpc.ui.theme.UncheckedTrack
import com.pht.vntechpc.utils.EnumConst
import com.pht.vntechpc.viewmodel.AddressFormState
import com.pht.vntechpc.viewmodel.AddressFormStatePage
import com.pht.vntechpc.viewmodel.AddressFormUiState
import com.pht.vntechpc.viewmodel.AddressFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressFormScreen(
    navController: NavController,
    viewModel: AddressFormViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        val address =
            navController.previousBackStackEntry?.savedStateHandle?.get<Address>(EnumConst.ADDRESS_KEY)
        if (address != null)
            viewModel.setAddress(address)
    }

    HandleSideEffect(state, viewModel, navController)

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (state.isEdit) "Sửa địa chỉ" else "Địa chỉ mới",
                        fontWeight = FontWeight.Medium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = TextOnPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        when (state.page) {
                            AddressFormStatePage.AddressForm -> navController.popBackStack()
                            AddressFormStatePage.AddressLocationPicker -> viewModel.backToAddressForm()
                        }
                    }) {
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
        when (state.page) {
            AddressFormStatePage.AddressForm -> AddressFormPage(innerPadding, viewModel, state)
            AddressFormStatePage.AddressLocationPicker -> AddressLocationPickerPage(
                innerPadding,
                viewModel,
                state
            )
        }
    }
}

@Composable
private fun AddressFormPage(
    innerPadding: PaddingValues,
    viewModel: AddressFormViewModel,
    state: AddressFormUiState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UnderlinedTextField(
            value = state.address.recipientName,
            onValueChange = { viewModel.updateRecipientName(it) },
            label = "Họ và tên",
            isError = !state.isRecipientNameValid,
            supportingText = state.recipientNameError,
        )

        VerticalSpacer(10)

        UnderlinedTextField(
            value = state.address.phoneNumber,
            onValueChange = { viewModel.updatePhoneNumber(it) },
            label = "Số điện thoại",
            keyboardType = KeyboardType.Phone,
            isError = !state.isPhoneNumberValid,
            supportingText = state.phoneNumberError,
        )

        VerticalSpacer(10)

        UnderlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { viewModel.goToAddressLocationPicker() },
            value = state.location,
            readOnly = true,
            enabled = false,
            label = "Địa chỉ",
            trailingIcon = R.drawable.chevron_right_24,
            textFieldColors = textFieldDisableColors()
        )
        VerticalSpacer(10)

        UnderlinedTextField(
            value = state.address.addressDetail,
            onValueChange = { viewModel.updateAddressDetail(it) },
            label = "Tên đường, Toà nhà, Số nhà",
            isError = !state.isAddressDetailValid,
            supportingText = state.addressDetailError,
        )

        VerticalSpacer(10)
        if (!state.isEdit)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Đặt làm địa chỉ mặc định")
                Switch(
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = CheckThumb,
                        checkedTrackColor = CheckTrack,
                        uncheckedTrackColor = UncheckedTrack,
                        uncheckedThumbColor = UncheckedThumb,
                        uncheckedBorderColor = UncheckBorder
                    ),
                    onCheckedChange = { viewModel.setIsDefault() },
                    checked = state.address.isDefault
                )
            }

        VerticalSpacer(10)

        if (state.isEdit) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                OutlinedButtonComponent(
                    onClick = { viewModel.confirmRemoveAddress() },
                    content = "Xoá địa chỉ"
                )

                FilledButtonComponent(onClick = {
                    viewModel.updateAddress(state.address)
                }, content = "Thay đổi")
            }

        } else {
            FilledButtonComponent(onClick = {
                viewModel.addNewAddress(state.address)
            }, content = "Thêm địa chỉ")
        }
    }
}

@Composable
private fun AddressLocationPickerPage(
    innerPadding: PaddingValues,
    viewModel: AddressFormViewModel,
    state: AddressFormUiState
) {
    LaunchedEffect(Unit) {
        viewModel.getProvinces()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DropDown(
            value = state.address.province,
            label = "Tỉnh/Thành phố",
            onClick = { viewModel.selectedProvince(it) },
            item = state.provinces.map { it.name }
        )

        VerticalSpacer(20)

        DropDown(
            value = state.address.district,
            label = "Quận/Huyện",
            onClick = { viewModel.selectedDistrict(it) },
            item = state.districts.map { it.name }
        )

        VerticalSpacer(20)

        DropDown(
            value = state.address.ward,
            label = "Phường",
            onClick = { viewModel.selectedWard(it) },
            item = state.wards.map { it.name }
        )

        VerticalSpacer(20)

        OutlinedButtonComponent(
            onClick = { viewModel.getLocation(state.address) },
            content = "Xác nhận",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropDown(
    value: String,
    label: String,
    onClick: (String) -> Unit,
    item: List<String>
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.fillMaxWidth()
    ) {
        UnderlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = true
                ),
            value = value,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            label = label,
            textFieldColors = textFieldDisableColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = !expanded }
        ) {
            Box(modifier = Modifier.background(Color.White)) {
                Column {
                    item.forEach {
                        DropdownMenuItem(
                            text = { Text(text = it) },
                            onClick = {
                                onClick(it)
                                expanded = !expanded
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = Color.Black
                            ),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HandleSideEffect(
    state: AddressFormUiState,
    viewModel: AddressFormViewModel,
    navController: NavController
) {
    when (val status = state.status) {
        is AddressFormState.Loading -> LoadingDialog(message = status.message)
        is AddressFormState.Success -> {
            navController.popBackStack()
        }

        is AddressFormState.Failure -> MessageDialog(
            message = status.message,
            onAction = { viewModel.setState() })

        is AddressFormState.None -> Unit
        is AddressFormState.ConfirmRemoveAddress -> ConfirmDialog(
            message = status.message,
            onConfirm = {
                viewModel.removeAddress(state.address.id)
            },
            onDismiss = { viewModel.setState() })
    }
}