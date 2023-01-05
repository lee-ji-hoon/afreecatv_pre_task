package com.android.data.datasource

import com.android.domain.common.ResultWrapper
import com.android.domain.model.BroadCategory
import com.android.domain.model.BroadList

interface BroadRemoteDataSource {

    suspend fun fetchBroadList(): ResultWrapper<BroadList>

    suspend fun fetchBrandCategoryList(): ResultWrapper<List<BroadCategory>>
}
