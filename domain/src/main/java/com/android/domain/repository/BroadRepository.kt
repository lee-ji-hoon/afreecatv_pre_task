package com.android.domain.repository

import com.android.domain.common.ResultWrapper
import com.android.domain.model.Broad
import com.android.domain.model.BroadCategory

interface BroadRepository {

    suspend fun fetchBroadList(categoryName: String): ResultWrapper<List<Broad>>

    suspend fun fetchBroadCategoryList(): ResultWrapper<List<BroadCategory>>
}
