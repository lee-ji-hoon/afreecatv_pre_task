package com.android.data.datasource

import com.android.data.NetworkApiService
import com.android.data.mapper.ConvertMapper
import com.android.data.model.BroadCategoryData
import com.android.data.model.BroadData
import com.android.domain.common.ResultWrapper
import com.android.domain.model.Broad
import com.android.domain.model.BroadCategory
import com.android.safecall.common.SafeApi
import javax.inject.Inject

class BrodRemoteDataSourceImpl @Inject constructor(
    private val apiService: NetworkApiService
) : BroadRemoteDataSource {

    private val safeApi = SafeApi()

    override suspend fun fetchBroadList(
        categoryName: String,
        pageNumber: Int
    ): ResultWrapper<List<Broad>> = safeApi.getSafe(remoteFetch = {
        apiService.fetchBroadList(
            categoryName = categoryName,
            pageNumber = pageNumber
        )
    }, mapping = { response ->
            response.broad.map {
                ConvertMapper<BroadData, Broad>()(
                    it
                )
            }
        })

    override suspend fun fetchBrandCategoryList(): ResultWrapper<List<BroadCategory>> =
        safeApi.getSafe(
            remoteFetch = { apiService.fetchBroadCategoryList() },
            mapping = { response ->
                response.categoryList.map {
                    ConvertMapper<BroadCategoryData, BroadCategory>()(
                        it
                    )
                }
            }
        )
}
