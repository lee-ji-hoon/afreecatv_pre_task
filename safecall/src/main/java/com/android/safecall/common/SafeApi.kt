package com.android.safecall.common

import com.android.domain.common.ErrorData
import com.android.domain.common.ResultWrapper
import com.android.safecall.error.ErrorHandlerImpl
import retrofit2.Response

class SafeApi : ErrorHandlerImpl() {

    override suspend fun <ResultType, RequestType> getSafe(
        remoteFetch: suspend () -> Response<RequestType>,
        mapping: (RequestType) -> ResultType
    ): ResultWrapper<ResultType> = handleResponse({ remoteFetch() }, mapping)

    private suspend fun <RequestType, ResultType> handleResponse(
        call: suspend () -> Response<RequestType>,
        converter: (RequestType) -> ResultType
    ): ResultWrapper<ResultType> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                response.body()?.let {
                    return ResultWrapper.Success(
                        data = converter(it),
                        code = response.code()
                    )
                }
            }
            return ResultWrapper.Failed(
                error = ErrorData.Api(response.errorBody()?.string())
            )
        } catch (t: Throwable) {
            ResultWrapper.Failed(getError(t))
        }
    }
}
