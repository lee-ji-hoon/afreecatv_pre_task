package com.android.domain.common

sealed class ResultWrapper<T>(
    val data: T? = null,
    val error: ErrorData? = null,
    val code: Int? = null
) {
    class Success<T>(
        data: T,
        code: Int? = null
    ) : ResultWrapper<T>(data = data, code = code)

    class Failed<T>(
        error: ErrorData,
        data: T? = null
    ) : ResultWrapper<T>(data = data, error = error)

    class FetchLoading<T>(
        data: T? = null
    ) : ResultWrapper<T>(data = data)

    override fun toString(): String {
        return when (this) {
            is Success -> "Success [data=$data]"
            is Failed -> "Error[exception=${error?.message}]"
            is FetchLoading -> "Fetching Loading"
        }
    }
}

val ResultWrapper<*>.succeeded
    get() = this is ResultWrapper.Success && data != null
