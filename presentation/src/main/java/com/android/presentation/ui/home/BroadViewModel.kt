package com.android.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.common.ResultWrapper
import com.android.domain.common.succeeded
import com.android.domain.model.Broad
import com.android.domain.usecase.FetchBroadListUseCase
import com.android.presentation.model.BroadUiModel
import com.android.presentation.model.toUiModel
import com.android.presentation.ui.common.MutableEventFlow
import com.android.presentation.ui.common.UiState
import com.android.presentation.ui.common.asEventFlow
import com.android.presentation.util.extenstion.manageError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BroadViewModel @Inject constructor(
    private val fetchBroadListUseCase: FetchBroadListUseCase
) : ViewModel() {

    private val _broadList = MutableStateFlow<List<BroadUiModel>?>(null)
    val broadList = _broadList.asStateFlow()

    private val _uiState: MutableEventFlow<UiState> = MutableEventFlow()
    val uiState = _uiState.asEventFlow()

    var pageNumber = BASIC_SIZE
        private set

    private var job: Job? = null

    fun fetchBroadList(categoryName: String) {
        if (job != null && job?.isActive == true) return

        job = viewModelScope.launch {
            _uiState.emit(UiState.Loading)
            val result = fetchBroadListUseCase(categoryName, pageNumber)
            if (result.succeeded) {
                emitSuccessData(result)
            } else {
                _uiState.emit(result.manageError())
            }
        }
    }

    private suspend fun emitSuccessData(result: ResultWrapper<List<Broad>>) {
        val data = result.data
        if (data.isNullOrEmpty() && broadList.value.isNullOrEmpty()) {
            _uiState.emit(UiState.EmptyResult)
        } else {
            pageNumber++
            _uiState.emit(UiState.Success(Unit))
        }
        data?.let { broadList ->
            val broadResult = broadList.map { it.toUiModel() }
            _broadList.value = _broadList.value?.plus(broadResult) ?: broadResult
        }
    }

    fun refresh(categoryName: String?) {
        viewModelScope.launch {
            _broadList.value = emptyList()
            pageNumber = BASIC_SIZE
            job?.cancelAndJoin()
            categoryName?.let { fetchBroadList(it) }
        }
    }

    companion object {
        private const val BASIC_SIZE = 1
    }
}
