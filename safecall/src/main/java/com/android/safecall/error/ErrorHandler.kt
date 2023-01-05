package com.android.safecall.error

import com.android.domain.common.ErrorData
import com.android.domain.common.ResultWrapper
import retrofit2.Response

interface ErrorHandler {

    suspend fun <ResultType, RequestType> getSafe(
        remoteFetch: suspend () -> Response<RequestType>,
        mapping: (RequestType) -> ResultType
    ): ResultWrapper<ResultType>

    fun getError(throwable: Throwable): ErrorData
}
