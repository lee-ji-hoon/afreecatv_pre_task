package com.android.data.repository

import com.android.data.datasource.BroadRemoteDataSource
import com.android.domain.common.ResultWrapper
import com.android.domain.model.BroadCategory
import com.android.domain.model.BroadList
import com.android.domain.repository.BroadRepository
import javax.inject.Inject

class BroadRepositoryImpl @Inject constructor(
    private val remoteDataSource: BroadRemoteDataSource
) : BroadRepository {

    override suspend fun fetchBroadList(): ResultWrapper<BroadList> {
        return remoteDataSource.fetchBroadList()
    }

    override suspend fun fetchBroadCategoryList(): ResultWrapper<List<BroadCategory>> {
        return remoteDataSource.fetchBrandCategoryList()
    }
}
