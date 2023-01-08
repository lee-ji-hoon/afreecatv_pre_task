package com.android.presentation.model

import com.android.domain.model.Broad
import java.io.Serializable

data class BroadUiModel(
    val number: String,
    val thumbnail: String,
    val title: String,
    val profileImage: String,
    val totalViewCount: String,
    val userId: String,
    val userNickname: String
) : Serializable

fun Broad.toUiModel(): BroadUiModel {
    return BroadUiModel(
        number = number,
        thumbnail = "$HTTPS$thumbnail",
        title = title,
        profileImage = "$HTTPS$profileImage",
        totalViewCount = totalViewCount,
        userId = userId,
        userNickname = userNickname
    )
}

private const val HTTPS = "https:"
