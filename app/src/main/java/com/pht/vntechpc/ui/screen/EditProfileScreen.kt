package com.pht.vntechpc.ui.screen

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pht.vntechpc.R
import com.pht.vntechpc.domain.model.User
import com.pht.vntechpc.ui.component.ConfirmDialog
import com.pht.vntechpc.ui.component.DatePickerDialog
import com.pht.vntechpc.ui.component.FilledButtonComponent
import com.pht.vntechpc.ui.component.ImagePickerBottomSheet
import com.pht.vntechpc.ui.component.LoadingDialog
import com.pht.vntechpc.ui.component.MessageDialog
import com.pht.vntechpc.ui.component.UnderlinedTextField
import com.pht.vntechpc.ui.component.VerticalSpacer
import com.pht.vntechpc.ui.component.textFieldDisableColors
import com.pht.vntechpc.ui.theme.Background
import com.pht.vntechpc.ui.theme.Border
import com.pht.vntechpc.ui.theme.DarkBackground
import com.pht.vntechpc.ui.theme.IconOnPrimary
import com.pht.vntechpc.ui.theme.TextOnPrimary
import com.pht.vntechpc.utils.EnumConst
import com.pht.vntechpc.viewmodel.EditProfileState
import com.pht.vntechpc.viewmodel.EditProfileUiState
import com.pht.vntechpc.viewmodel.EditProfileViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        val user = navController.previousBackStackEntry?.savedStateHandle?.get<User>(EnumConst.USER_KEY)
        if (user != null) {
            viewModel.setUser(user)
        }
    }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text(text = "Chỉnh sửa hồ sơ", fontWeight = FontWeight.Medium) },
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
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(2.dp, Border, CircleShape),
            ) {
                AsyncImage(
                    model = state.avatarLocalUri ?: state.avatar,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    placeholder = painterResource(R.drawable.default_avatar),
                    error = painterResource(R.drawable.default_avatar)
                )

                IconButton(
                    onClick = { viewModel.showBottomSheetGetPhoto() },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.camera_alt_24),
                        contentDescription = null,
                    )
                }
            }

            VerticalSpacer(10)

            if (state.avatarLocalUri != null) {
                FilledButtonComponent(onClick = {
                    try {
                        viewModel.uploadAvatar(uriToFile(state.avatarLocalUri!!, context))
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }
                }, content = "Thay đổi ảnh đại diện")
            }

            Spacer(modifier = Modifier.height(20.dp))

            UnderlinedTextField(
                value = state.fullName,
                onValueChange = { viewModel.updateFullName(it) },
                label = "Họ và tên",
                isError = !state.isValid,
                supportingText = state.fullNameError
            )

            VerticalSpacer(10)

            UnderlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        viewModel.showDialogGetTime()
                    },
                value = state.dateOfBirth,
                onValueChange = { },
                label = "Ngày sinh",
                readOnly = true,
                enabled = false,
                textFieldColors = textFieldDisableColors()
            )

            VerticalSpacer(20)

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                UnderlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(
                            type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                            enabled = true
                        ),
                    value = state.gender,
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    label = "Giới tính",
                    textFieldColors = textFieldDisableColors()
                )

                val item = listOf("Nam", "Nữ", "Khác")
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
                                        viewModel.updateGender(it)
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

            VerticalSpacer(20)

            FilledButtonComponent(onClick = {
                viewModel.updateProfile()
            }, content = "Lưu thay đổi", enable = state.isValid)
        }
    }

    HandleEditProfileSideEffects(context, state, viewModel, navController)
}

private fun createImageUri(context: Context): Uri? {
    val imagesFolder = File(context.cacheDir, "images")
    imagesFolder.mkdirs()
    val file = File(imagesFolder, "camera_photo_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
}

private fun uriToFile(uri: Uri, context: Context): File {
    val inputStream = context.contentResolver.openInputStream(uri)
        ?: throw IllegalArgumentException("Cannot open input stream from URI")
    val tempFile = File.createTempFile("temp_image", ".jpg", context.cacheDir)
    tempFile.outputStream().use { outputStream ->
        inputStream.copyTo(outputStream)
    }
    return tempFile
}

@Composable
private fun HandleEditProfileSideEffects(
    context: Context,
    state: EditProfileUiState,
    viewModel: EditProfileViewModel,
    navController: NavController
) {
    when (val status = state.status) {

        is EditProfileState.GetTime -> {
            val datePickerState =
                rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
            DatePickerDialog(
                datePickerState = datePickerState,
                onDismissRequest = { viewModel.resetState() },
                onConfirm = { viewModel.updateBirthOfDay(datePickerState.selectedDateMillis) })
        }

        is EditProfileState.GetPhoto -> {
            val galleryLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri ->
                uri?.let {
                    viewModel.updateAvatarWithUri(uri)
                }
                viewModel.resetState()
            }

            val cameraImageUri = remember { mutableStateOf<Uri?>(null) }

            val cameraLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.TakePicture()
            ) { success ->
                if (success) {
                    cameraImageUri.value?.let { uri ->
                        viewModel.updateAvatarWithUri(uri)
                    }
                }
                viewModel.resetState()
            }
            ImagePickerBottomSheet(
                onCamera = {
                    val uri = createImageUri(context)
                    cameraImageUri.value = uri
                    uri?.let {
                        cameraLauncher.launch(uri)
                    }
                },
                onGallery = {
                    galleryLauncher.launch("image/*")
                },
                onRemove = { viewModel.removeAvatar() },
                onDismiss = { viewModel.resetState() }
            )
        }

        is EditProfileState.ConfirmRemoveAvatar -> {
            ConfirmDialog(message = status.message, onConfirm = {
                viewModel.deleteAvatar()
            }, onDismiss = {
                viewModel.resetState()
            })
        }

        is EditProfileState.Success -> {
            MessageDialog(message = status.message, onAction = {
                viewModel.resetState()
                navController.popBackStack()
            })
        }

        is EditProfileState.Failure -> {
            MessageDialog(message = status.message, onAction = { viewModel.resetState() })
        }

        is EditProfileState.Loading -> LoadingDialog(message = status.message)
        else -> Unit
    }
}
