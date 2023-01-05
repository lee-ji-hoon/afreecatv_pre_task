package com.android.afreecasampleapp

import okhttp3.Interceptor
import okhttp3.Response

class HTTPRequestInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val origin = chain.request()
        val request = origin.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("client_id", BuildConfig.CLIENT_ID)
            .build()
        return chain.proceed(request)
    }
}
