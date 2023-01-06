package com.android.data.datasource

import com.android.domain.common.ResultWrapper
import com.android.domain.model.Broad
import com.android.domain.model.BroadCategory

interface BroadRemoteDataSource {

    suspend fun fetchBroadList(categoryName: String): ResultWrapper<List<Broad>>

    suspend fun fetchBrandCategoryList(): ResultWrapper<List<BroadCategory>>
}
