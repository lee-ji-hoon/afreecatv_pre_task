package com.android.data.datasource

import com.android.data.NetworkApiService
import com.android.data.mapper.ConvertMapper
import com.android.data.model.BroadCategoryListData
import com.android.data.model.BroadListData
import com.android.domain.common.ResultWrapper
import com.android.domain.model.BroadCategory
import com.android.domain.model.BroadList
import com.android.safecall.common.SafeApi
import javax.inject.Inject

class BrodRemoteDataSourceImpl @Inject constructor(
    private val apiService: NetworkApiService
) : BroadRemoteDataSource, SafeApi() {

    override suspend fun fetchBroadList(): ResultWrapper<BroadList> = getSafe(
        remoteFetch = { apiService.fetchBroadList() },
        mapping = { response -> ConvertMapper<BroadListData, BroadList>()(response) }
    )

    override suspend fun fetchBrandCategoryList(): ResultWrapper<List<BroadCategory>> = getSafe(
        remoteFetch = { apiService.fetchBroadCategoryList() },
        mapping = { response ->
            response.categoryList.map {
                ConvertMapper<BroadCategoryListData.BroadCategoryData, BroadCategory>()(
                    it
                )
            }
        }
    )
}
