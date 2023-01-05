package com.android.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.common.ErrorData
import com.android.domain.common.ResultWrapper
import com.android.domain.common.succeeded
import com.android.domain.model.BroadCategory
import com.android.domain.usecase.FetchBroadCategoryUseCase
import com.android.presentation.R
import com.android.presentation.ui.common.MutableEventFlow
import com.android.presentation.ui.common.UiState
import com.android.presentation.ui.common.asEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val fetchBroadCategoryUseCase: FetchBroadCategoryUseCase
) : ViewModel() {

    private val _category = MutableStateFlow<List<BroadCategory>?>(null)
    val category = _category.asStateFlow()

    private val _uiState: MutableEventFlow<UiState<List<BroadCategory>>> = MutableEventFlow()
    val uiState = _uiState.asEventFlow()

    init {
        viewModelScope.launch {
            fetchBroadCategoryUseCase().manageResult()?.let {
                _category.value = it
            }
        }
    }

    private suspend fun <T> ResultWrapper<T>.manageResult(): T? {
        if (succeeded) return data

        when (error) {
            is ErrorData.Network -> _uiState.emit(UiState.Failure(R.string.error_network))
            is ErrorData.Server -> _uiState.emit(UiState.Failure(R.string.error_server))
            is ErrorData.Api -> _uiState.emit(UiState.Failure(R.string.error_api))
            else -> _uiState.emit(UiState.Failure(R.string.error_unknown))
        }
        return null
    }
}
