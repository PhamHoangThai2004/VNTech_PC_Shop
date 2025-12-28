package com.pht.vntechpc.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pht.vntechpc.data.local.UserPreferences
import com.pht.vntechpc.domain.model.User
import com.pht.vntechpc.domain.usecase.user.RemoveAvatarUseCase
import com.pht.vntechpc.domain.usecase.user.UpdateProfileUseCase
import com.pht.vntechpc.domain.usecase.user.UploadAvatarUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val uploadAvatarUseCase: UploadAvatarUseCase,
    private val removeAvatarUseCase: RemoveAvatarUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState
    private var _user: User? = null

    fun updateFullName(fullName: String) {
        if (fullName.trim().length > 255) {
            _uiState.value = _uiState.value.copy(
                fullName = fullName ,
                isValid = false,
                fullNameError = "Tên quá dài"
            )
        } else if (fullName.trim().isNotEmpty()) {
            _uiState.value = _uiState.value.copy(
                fullName = fullName,
                isValid = true,
                fullNameError = ""
            )
        } else {
            _uiState.value = _uiState.value.copy(
                fullName = fullName,
                isValid = false,
                fullNameError = "Yêu cầu nhập tên"
            )
        }
    }

    fun updateGender(gender: String) {
        _uiState.value = _uiState.value.copy(gender = gender)
    }

    fun updateBirthOfDay(millis: Long?) {
        _uiState.value = _uiState.value.copy(
            dateOfBirth = convertMillisToDate(millis),
            status = EditProfileState.None
        )
    }

    fun showDialogGetTime() {
        _uiState.value = _uiState.value.copy(status = EditProfileState.GetTime)
    }

    fun showBottomSheetGetPhoto() {
        _uiState.value = _uiState.value.copy(status = EditProfileState.GetPhoto)
    }

    fun setUser(user: User) {
        _user = user
        _uiState.value = _uiState.value.copy(
            fullName = user.fullName ?: "",
            gender = user.gender ?: "Chưa có",
            dateOfBirth = user.dateOfBirth ?: "Chưa có",
            avatar = user.avatar ?: ""
        )
    }

    fun resetState() {
        _uiState.value = _uiState.value.copy(status = EditProfileState.None)
    }

    fun updateProfile() {
        if (_user == null) return
        _uiState.value = _uiState.value.copy(status = EditProfileState.Loading("Đang cập nhật..."))
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                updateProfileUseCase(
                    _user?.email ?: "",
                    _uiState.value.fullName,
                    _uiState.value.gender,
                    _uiState.value.dateOfBirth
                )
            }

            result.onSuccess {
                withContext(Dispatchers.IO) {
                    userPreferences.saveUser(
                        _user!!.copy(
                            fullName = _uiState.value.fullName,
                            gender = _uiState.value.gender,
                            dateOfBirth = _uiState.value.dateOfBirth
                        )
                    )
                }
                Log.d("BBB", "Update profile success: ${it.message}")
                _uiState.value = _uiState.value.copy(
                    status = EditProfileState.Success(it.message)
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    status = EditProfileState.Failure(it.message.toString())
                )
            }
        }
    }

    fun updateAvatarWithUri(uri: Uri) {
        _uiState.value = _uiState.value.copy(
            avatarLocalUri = uri
        )
        Log.d("BBB", "updateAvatarWithUri: $uri")
    }

    fun removeAvatar() {
        if (_uiState.value.avatarLocalUri != null) {
            _uiState.value =
                _uiState.value.copy(avatarLocalUri = null, status = EditProfileState.None)
        } else {
            _uiState.value =
                _uiState.value.copy(status = EditProfileState.ConfirmRemoveAvatar("Bạn có chắc chắn muốn xóa ảnh đại diện?"))
        }
    }

    fun uploadAvatar(file: File) {
        if (_user == null || _uiState.value.avatarLocalUri == null) return
        _uiState.value = _uiState.value.copy(status = EditProfileState.Loading("Đang tải lên..."))
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                uploadAvatarUseCase(_user!!.id, file)
            }

            result.onSuccess {
                withContext(Dispatchers.IO) {
                    userPreferences.saveUser(it)
                }
                _uiState.value = _uiState.value.copy(
                    status = EditProfileState.Success("Cập nhật ảnh đại diện thành công")
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    status = EditProfileState.Failure(it.message.toString())
                )
            }
        }
    }

    fun deleteAvatar() {
        if (_user == null) return
        _uiState.value = _uiState.value.copy(status = EditProfileState.Loading("Đang xoá..."))
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                removeAvatarUseCase(_user!!.id)
            }
            result.onSuccess {
                withContext(Dispatchers.IO) {
                    _user = _user!!.copy(avatar = null)
                    userPreferences.saveUser(_user!!)
                }
                _uiState.value = _uiState.value.copy(
                    status = EditProfileState.Success(it.message)
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    status = EditProfileState.Failure(it.message.toString())
                )
            }
        }
    }

    private fun convertMillisToDate(millis: Long?): String {
        if (millis == null) return ""

        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return sdf.format(Date(millis))
    }
}

data class EditProfileUiState(
    val fullName: String = "",
    val gender: String = "Chưa có",
    val dateOfBirth: String = "Chưa có",
    val avatar: String = "",
    val avatarLocalUri: Uri? = null,
    val isValid: Boolean = true,
    val fullNameError: String = "",
    val status: EditProfileState = EditProfileState.None
)

sealed class EditProfileState {
    object None : EditProfileState()
    object GetTime : EditProfileState()
    object GetPhoto : EditProfileState()
    data class Loading(val message: String) : EditProfileState()
    data class ConfirmRemoveAvatar(val message: String) : EditProfileState()
    data class Success(val message: String) : EditProfileState()
    data class Failure(val message: String) : EditProfileState()
}