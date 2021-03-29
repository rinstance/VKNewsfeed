package com.example.data.di

import com.example.data.helpers.Constants
import com.example.data.network.VKApi
import com.example.data.network.interceptors.ApiConstInterceptor
import com.example.data.network.interceptors.LoggingInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApiModule {
    @Provides
    @Singleton
    fun provideClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(ApiConstInterceptor())
            .addInterceptor(LoggingInterceptor())
            .build()

    @Provides
    @Singleton
    fun buildRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(client)
            .baseUrl(Constants.BASE_METHODS_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideVKApi(retrofit: Retrofit): VKApi = retrofit.create(VKApi::class.java)
}