package com.pht.vntechpc.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pht.vntechpc.data.local.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import jakarta.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userPreferences: UserPreferences) :
    ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUIState())
    val uiState: StateFlow<ProfileUIState> = _uiState

    fun getUser() {
        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) { userPreferences.getUser() }
            Log.d("BBB", "Profile: $user")
            _uiState.value = _uiState.value.copy(
                fullName = user?.fullName,
                avatar = user?.avatar,
                userId = user?.id
            )
        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferences.clear()
        }
    }
}

data class ProfileUIState(
    val fullName: String? = null,
    val avatar: String? = null,
    val userId : Int? = null
)

