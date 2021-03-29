package com.example.vknewsfeed

import android.app.Application
import com.example.vknewsfeed.di.AppComponent
import com.example.vknewsfeed.di.DaggerAppComponent

class App : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .application(this)
            .build()
    }
}