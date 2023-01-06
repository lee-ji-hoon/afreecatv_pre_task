package com.android.presentation.util.extenstion

import com.android.domain.common.ErrorData
import com.android.domain.common.ResultWrapper
import com.android.domain.common.succeeded
import com.android.presentation.R
import com.android.presentation.ui.common.MutableEventFlow
import com.android.presentation.ui.common.UiState

suspend fun <T> ResultWrapper<T>.manageResult(state: MutableEventFlow<UiState>): T? {
    if (succeeded) return data

    when (error) {
        is ErrorData.Network -> state.emit(UiState.Failure(R.string.error_network))
        is ErrorData.Server -> state.emit(UiState.Failure(R.string.error_server))
        is ErrorData.Api -> state.emit(UiState.Failure(R.string.error_api))
        else -> state.emit(UiState.Failure(R.string.error_unknown))
    }
    return null
}
