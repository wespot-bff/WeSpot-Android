package com.bff.wespot.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {
    @Provides
    @Singleton
    fun provideIoDispatcher() = Dispatchers.IO

    @Provides
    @Singleton
    fun provideMainDispatcher() = Dispatchers.Main
}