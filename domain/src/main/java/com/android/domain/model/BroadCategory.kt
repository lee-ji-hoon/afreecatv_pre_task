package com.android.domain.model

data class BroadCategory(
    val name: String,
    val number: String,
    val subCategoryList: List<SubCategory>
) {
    data class SubCategory(
        val name: String,
        val number: String
    )
}
