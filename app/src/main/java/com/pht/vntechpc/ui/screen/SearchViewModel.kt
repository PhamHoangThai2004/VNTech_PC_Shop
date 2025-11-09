package com.pht.vntechpc.ui.screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    fun updateKeyword(keyword: String) {
        _uiState.value = _uiState.value.copy(keyword = keyword)
    }

    fun goToSearchInputPage() {
        _uiState.value = _uiState.value.copy(page = SearchStatePage.SearchInputPage)
    }

    fun backToSearchMainPage() {
        _uiState.value = _uiState.value.copy(page = SearchStatePage.SearchMainPage)
    }
}

sealed class SearchStatePage {
    object SearchMainPage : SearchStatePage()
    object SearchInputPage : SearchStatePage()
}

data class SearchUiState(
    val keyword: String = "",
    val page: SearchStatePage = SearchStatePage.SearchMainPage
)