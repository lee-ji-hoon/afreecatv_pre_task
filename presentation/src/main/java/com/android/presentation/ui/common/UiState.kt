package com.android.presentation.ui.common

import androidx.annotation.StringRes

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<out T>(val item: T) : UiState<T>()
    data class Failure(@StringRes val message: Int) : UiState<Nothing>()
}
