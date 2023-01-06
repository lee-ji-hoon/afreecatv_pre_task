package com.android.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.usecase.FetchBroadListUseCase
import com.android.presentation.model.BroadUiModel
import com.android.presentation.model.toUiModel
import com.android.presentation.ui.common.MutableEventFlow
import com.android.presentation.ui.common.UiState
import com.android.presentation.ui.common.asEventFlow
import com.android.presentation.util.extenstion.manageResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BroadViewModel @Inject constructor(
    private val fetchBroadListUseCase: FetchBroadListUseCase
) : ViewModel() {

    private val _broadList = MutableStateFlow<List<BroadUiModel>?>(null)
    val broadList = _broadList.asStateFlow()

    private val _uiState: MutableEventFlow<UiState> = MutableEventFlow()
    val uiState = _uiState.asEventFlow()

    fun fetchBroadList(categoryName: String) {
        viewModelScope.launch {
            fetchBroadListUseCase(categoryName).manageResult(_uiState)?.let { broadListData ->
                Timber.tag("TAG").d("${javaClass.simpleName} data -> $broadListData")
                val broadResult = broadListData.map { it.toUiModel() }
                _broadList.value = _broadList.value?.plus(broadResult) ?: broadResult
            }
        }
    }
}
