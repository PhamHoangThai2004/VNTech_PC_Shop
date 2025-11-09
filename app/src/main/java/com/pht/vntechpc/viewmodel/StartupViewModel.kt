package com.pht.vntechpc.viewmodel

import androidx.lifecycle.ViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StartupViewModel @Inject constructor() : ViewModel() {
    private val _isLoggedIn = MutableStateFlow<Boolean?>(false)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn

    init {
        _isLoggedIn.value = false
    }
}