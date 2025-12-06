package com.pht.vntechpc.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pht.vntechpc.data.local.UserPreferences
import com.pht.vntechpc.domain.usecase.auth.RefreshTokenUseCase
import com.pht.vntechpc.domain.usecase.user.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class StartupViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val getProfileUseCase: GetProfileUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<StartupState>(StartupState.Pressing)
    val state: StateFlow<StartupState> = _state

    init {
        getProfile()
    }

    fun getProfile() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                getProfileUseCase()
            }
            result.onSuccess {
                userPreferences.saveUser(it)
                _state.value = StartupState.LoggedIn
            }.onFailure {
                Log.d("BBB", "Startup failure: ${it.message}")
                refreshToken()
            }
        }
    }

    fun refreshToken() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                refreshTokenUseCase(userPreferences.getRefreshToken() ?: "")
            }
            result.onSuccess {
                userPreferences.saveAccessToken(it.accessToken)
                userPreferences.saveRefreshToken(it.refreshToken)
                getProfile()
            }.onFailure {
                Log.d("BBB", "Refresh failure: ${it.message}")
                _state.value = StartupState.LoggedOut
            }
        }
    }
}

sealed class StartupState {
    object Pressing : StartupState()
    object LoggedIn : StartupState()
    object LoggedOut : StartupState()
}