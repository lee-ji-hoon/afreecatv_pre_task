package com.android.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BroadCategoryListData(
    @field:Json(name = "broad_category") val categoryList: List<BroadCategoryData>
)

@JsonClass(generateAdapter = true)
data class BroadCategoryData(
    @field:Json(name = "cate_name") val name: String,
    @field:Json(name = "cate_no") val number: String,
    @field:Json(name = "child") val subCategoryList: List<SubCategoryData>
) {
    @JsonClass(generateAdapter = true)
    data class SubCategoryData(
        @field:Json(name = "cate_name") val name: String,
        @field:Json(name = "cate_no") val number: String
    )
}
