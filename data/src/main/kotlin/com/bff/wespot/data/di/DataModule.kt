package com.bff.wespot.data.di

import com.bff.wespot.data.local.WeSpotDataStore
import com.bff.wespot.data.local.WeSpotDataStoreImpl
import com.bff.wespot.data.repository.auth.KakaoLoginManagerImpl
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.data.repository.message.MessageRepositoryImpl
import com.bff.wespot.domain.repository.auth.KakaoLoginManager
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

    @Binds
    @Singleton
    abstract fun bindsKakaoLoginManager(
        kakaoLoginManagerImpl: KakaoLoginManagerImpl
    ): KakaoLoginManager

    @Binds
    @Singleton
    abstract fun bindsWeSpotDataStore(
        weSpotDataStoreImpl: WeSpotDataStoreImpl
    ): WeSpotDataStore
}
