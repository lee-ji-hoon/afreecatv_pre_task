package com.android.data

import com.android.data.model.BroadCategoryListData
import com.android.data.model.BroadListData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkApiService {

    @GET("broad/list")
    suspend fun fetchBroadList(
        @Query("client_id") clientId: String = BuildConfig.CLIENT_ID,
        @Query("select_value") categoryName: String,
        @Query("page_no") pageNumber: Int = 1
    ): Response<BroadListData>

    @GET("broad/category/list")
    suspend fun fetchBroadCategoryList(
        @Query("client_id") clientId: String = BuildConfig.CLIENT_ID
    ): Response<BroadCategoryListData>
}
