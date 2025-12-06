package com.pht.vntechpc.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.pht.vntechpc.data.local.UserPreferences
import androidx.lifecycle.viewModelScope
import com.pht.vntechpc.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AccountViewModel @Inject constructor(private val userPreferences: UserPreferences) :
    ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun getUser() {
        viewModelScope.launch {
            val user = userPreferences.getUser()
            Log.d("BBB", "Profile: $user")
            _user.value = user
        }
    }
}