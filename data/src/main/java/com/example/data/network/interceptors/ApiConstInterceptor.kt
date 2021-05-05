package com.example.data.network.interceptors

import com.example.domain.BuildConfig
import com.example.domain.helpers.Constants
import okhttp3.Interceptor
import okhttp3.Response

class ApiConstInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        return original.url().newBuilder()
            .addQueryParameter("access_token", Constants.TOKEN)
            .addQueryParameter("v", Constants.api)
            .build()
            .let {
                chain.proceed(original.newBuilder().url(it).build())
            }
    }
}