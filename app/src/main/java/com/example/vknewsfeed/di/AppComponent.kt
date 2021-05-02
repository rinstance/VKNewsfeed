package com.example.vknewsfeed.di

import android.content.Context
import com.example.data.di.ApiModule
import com.example.data.di.AppModule
import com.example.data.di.DatabaseModule
import com.example.data.di.RepositoryModule
import com.example.vknewsfeed.activities.main.di.MainComponent
import com.example.vknewsfeed.detail.di.DetailComponent
import com.example.vknewsfeed.favorites.di.FavouritesComponent
import com.example.vknewsfeed.newsfeed.di.NewsfeedComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApiModule::class,
        DatabaseModule::class,
        AppModule::class,
        RepositoryModule::class,
        HelperModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Context): Builder

        fun build(): AppComponent
    }

    fun newsComponentBuilder(): NewsfeedComponent.Factory
    fun detailComponentFactory(): DetailComponent.Factory
    fun favouritesComponentBuilder(): FavouritesComponent.Factory
    fun mainComponentFactory(): MainComponent.Factory
}
