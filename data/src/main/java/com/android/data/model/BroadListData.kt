package com.android.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BroadListData(
    @field:Json(name = "broad") val broad: List<BroadData>,
    @field:Json(name = "page_no") val pageNo: Int,
    @field:Json(name = "time") val time: Int,
    @field:Json(name = "total_cnt") val totalCount: Int
) {
    @JsonClass(generateAdapter = true)
    data class BroadData(
        @field:Json(name = "broad_bps") val bps: String,
        @field:Json(name = "broad_cate_no") val categoryNo: String,
        @field:Json(name = "broad_grade") val grade: String,
        @field:Json(name = "broad_no") val number: String,
        @field:Json(name = "broad_resolution") val resolution: String,
        @field:Json(name = "broad_start") val startTime: String,
        @field:Json(name = "broad_thumb") val thumbnail: String,
        @field:Json(name = "broad_title") val title: String,
        @field:Json(name = "is_password") val isPassword: String,
        @field:Json(name = "profile_img") val profileImage: String,
        @field:Json(name = "total_view_cnt") val totalViewCount: String,
        @field:Json(name = "user_id") val userId: String,
        @field:Json(name = "user_nick") val userNickname: String,
        @field:Json(name = "visit_broad_type") val visitBroadType: String
    )
}
