package com.bff.wespot.data.remote.di

import com.bff.wespot.data.remote.source.auth.AuthDataSource
import com.bff.wespot.data.remote.source.auth.AuthDataSourceImpl
import com.bff.wespot.data.remote.source.message.MessageDataSource
import com.bff.wespot.data.remote.source.message.MessageDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataRemoteModule {
    @Binds
    @Singleton
    abstract fun bindsMessageDataSource(
        messageDataSourceImpl: MessageDataSourceImpl
    ): MessageDataSource

    @Binds
    @Singleton
    abstract fun bindsAuthDataSource(
        authDataSourceImpl: AuthDataSourceImpl
    ): AuthDataSource
}