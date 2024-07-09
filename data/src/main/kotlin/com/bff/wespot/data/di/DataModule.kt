package com.bff.wespot.data.di

import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.data.repository.message.MessageRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun bindsMessageRepository(
        messageRepository: MessageRepositoryImpl
    ): MessageRepository
}
