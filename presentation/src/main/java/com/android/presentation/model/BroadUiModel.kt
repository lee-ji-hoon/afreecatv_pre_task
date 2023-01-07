package com.android.presentation.model

import com.android.domain.model.Broad
import java.io.Serializable

data class BroadUiModel(
    val bps: String,
    val categoryNo: String,
    val grade: String,
    val number: String,
    val resolution: String,
    val startTime: String,
    val thumbnail: String,
    val title: String,
    val isPassword: String,
    val profileImage: String,
    val totalViewCount: String,
    val userId: String,
    val userNickname: String,
    val visitBroadType: String
) : Serializable

fun Broad.toUiModel(): BroadUiModel {
    return BroadUiModel(
        bps = bps,
        categoryNo = categoryNo,
        grade = grade,
        number = number,
        resolution = resolution,
        startTime = startTime,
        thumbnail = "$HTTPS$thumbnail",
        title = title,
        isPassword = isPassword,
        profileImage = "$HTTPS$profileImage",
        totalViewCount = totalViewCount,
        userId = userId,
        userNickname = userNickname,
        visitBroadType = visitBroadType
    )
}

private const val HTTPS = "https:"
