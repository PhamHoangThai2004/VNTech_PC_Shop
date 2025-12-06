package com.pht.vntechpc.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pht.vntechpc.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickerBottomSheet(
    onCamera: () -> Unit,
    onGallery: () -> Unit,
    onRemove: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {
        val interactionSource = remember { MutableInteractionSource() }

        Column(
            modifier = Modifier
                .fillMaxWidth().wrapContentWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            options.forEach { it ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            when (it.name) {
                                "CAMERA" -> onCamera()
                                "GALLERY" -> onGallery()
                                "REMOVE" -> onRemove()
                            }
                        }
                ) {
                    Icon(
                        painter = painterResource(id = it.icon),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(5.dp))

                    Text(it.title)
                }
            }


        }
    }
}

sealed class OptionImagePicker (
    val name: String,
    val title: String,
    val icon: Int
) {
    object Camera : OptionImagePicker("CAMERA","Chụp ảnh", R.drawable.camera_alt_24)
    object Gallery : OptionImagePicker("GALLERY","Chọn ảnh từ thư viện", R.drawable.photo_24)
    object Remove : OptionImagePicker("REMOVE","Xoá ảnh", R.drawable.delete_24)
}

private val options = listOf(
    OptionImagePicker.Camera,
    OptionImagePicker.Gallery,
    OptionImagePicker.Remove
)

