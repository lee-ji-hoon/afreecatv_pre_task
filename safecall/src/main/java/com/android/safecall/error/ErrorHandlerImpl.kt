package com.android.safecall.error

import android.database.sqlite.SQLiteException
import com.android.domain.common.ErrorData
import com.android.domain.common.NoInternetException
import com.android.domain.common.ServerConnectionException
import retrofit2.HttpException
import java.net.SocketException
import java.net.UnknownHostException

abstract class ErrorHandlerImpl : ErrorHandler {

    override fun getError(throwable: Throwable): ErrorData {
        return when (throwable) {
            is UnknownHostException,
            is SocketException,
            is NoInternetException -> ErrorData.Network
            is ServerConnectionException -> ErrorData.Server(message = "${throwable.message}")
            is SQLiteException -> ErrorData.Database(message = "${throwable.message}")
            is HttpException -> ErrorData.Api(
                message = throwable.response()?.message(),
                code = throwable.code(),
                errorBody = throwable.response()?.errorBody()?.string()
            )
            else -> ErrorData.Unknown(message = "${throwable.message}")
        }
    }
}
