package com.android.presentation.model

import com.android.domain.model.BroadCategory

data class BroadCategoryUiModel(
    val name: String,
    val number: String
)

fun BroadCategory.toUiModel(): BroadCategoryUiModel {
    return BroadCategoryUiModel(
        name = name,
        number = number
    )
}
