package com.example.vknewsfeed.di

import androidx.lifecycle.ViewModel
import com.example.vknewsfeed.MainViewModel
import dagger.MapKey
import kotlin.reflect.KClass

@Target(
    AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@MapKey
annotation class ViewModelKey(val value: KClass<out MainViewModel>)