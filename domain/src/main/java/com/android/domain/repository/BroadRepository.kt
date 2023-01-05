package com.android.domain.repository

import com.android.domain.common.ResultWrapper
import com.android.domain.model.BroadCategory
import com.android.domain.model.BroadList

interface BroadRepository {

    suspend fun fetchBroadList(): ResultWrapper<BroadList>

    suspend fun fetchBroadCategoryList(): ResultWrapper<List<BroadCategory>>
}
