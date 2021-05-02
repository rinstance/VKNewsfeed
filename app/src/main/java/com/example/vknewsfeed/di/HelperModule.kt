package com.example.vknewsfeed.di

import com.example.vknewsfeed.helpers.ImageHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class HelperModule {
    @Provides
    @Singleton
    fun provideImageHelper() = ImageHelper()
}