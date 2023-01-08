package com.android.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.common.succeeded
import com.android.domain.usecase.FetchBroadCategoryUseCase
import com.android.presentation.model.BroadCategoryUiModel
import com.android.presentation.model.toUiModel
import com.android.presentation.ui.common.MutableEventFlow
import com.android.presentation.ui.common.UiState
import com.android.presentation.ui.common.asEventFlow
import com.android.presentation.util.extenstion.manageError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchBroadCategoryUseCase: FetchBroadCategoryUseCase
) : ViewModel() {

    private val _category = MutableStateFlow<List<BroadCategoryUiModel>?>(null)
    val category = _category.asStateFlow()

    private val _uiState: MutableEventFlow<UiState> = MutableEventFlow()
    val uiState = _uiState.asEventFlow()

    init {
        viewModelScope.launch {
            _uiState.emit(UiState.Loading)
            val result = fetchBroadCategoryUseCase()
            if (result.succeeded) {
                _uiState.emit(UiState.Success(Unit))
                _category.value = result.data?.map { it.toUiModel() }
            } else {
                _uiState.emit(result.manageError())
            }
        }
    }
}
