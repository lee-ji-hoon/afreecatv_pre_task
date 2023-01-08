package com.android.presentation.util.extenstion

import com.android.domain.common.ErrorData
import com.android.domain.common.ResultWrapper
import com.android.presentation.R
import com.android.presentation.ui.common.UiState

fun <T> ResultWrapper<T>.manageError(): UiState {
    return when (error) {
        is ErrorData.Network -> UiState.NetworkFailure
        is ErrorData.Server -> UiState.Failure(R.string.error_server)
        is ErrorData.Api -> UiState.Failure(R.string.error_api)
        else -> UiState.Failure(R.string.error_unknown)
    }
}
