package com.pht.vntechpc.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pht.vntechpc.data.local.UserPreferences
import com.pht.vntechpc.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userPreferences: UserPreferences) :
    ViewModel() {
    init {
        getUser()
    }

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private fun getUser() {
        viewModelScope.launch {
            val user = userPreferences.getUser()
            Log.d("BBB", "Profile: $user")
            _user.value = user
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPreferences.clear()
        }
    }
}

