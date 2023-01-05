package com.android.presentation.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.usecase.FetchBroadCategoryUseCase
import com.android.domain.usecase.FetchBroadListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val fetchBroadListUseCase: FetchBroadListUseCase,
    private val fetchBroadCategoryUseCase: FetchBroadCategoryUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            val broadList = fetchBroadListUseCase()
            val category = fetchBroadCategoryUseCase()
            Log.d("TAG", "방송 리스트 -> $broadList ")
            Log.d("TAG", "카테고리 리스트 -> $category ")
        }
    }
}
