package com.android.data.model

import com.squareup.moshi.Json

data class BroadCategoryListData(
    @field:Json(name = "broad_category") val categoryList: List<BroadCategory>
)

data class BroadCategory(
    @field:Json(name = "cate_name") val name: String,
    @field:Json(name = "cate_no") val number: String,
    @field:Json(name = "child") val subCategoryList: List<SubCategory>
)

data class SubCategory(
    @field:Json(name = "cate_name") val name: String,
    @field:Json(name = "cate_no") val number: String
)
