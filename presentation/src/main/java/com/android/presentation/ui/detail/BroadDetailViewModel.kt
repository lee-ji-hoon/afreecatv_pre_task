package com.android.presentation.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.android.presentation.model.BroadUiModel
import com.android.presentation.util.Extras.KEY_BROAD_MODEL
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BroadDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val broad = savedStateHandle.getStateFlow<BroadUiModel?>(KEY_BROAD_MODEL, null)
}
