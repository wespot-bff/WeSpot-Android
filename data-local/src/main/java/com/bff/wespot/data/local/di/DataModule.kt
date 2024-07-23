package com.bff.wespot.data.local.di

import com.bff.wespot.data.local.source.WeSpotDataStore
import com.bff.wespot.data.local.source.WeSpotDataStoreImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    @Singleton
    fun bindsWeSpotDataStore(
        weSpotDataStoreImpl: WeSpotDataStoreImpl
    ): WeSpotDataStore
}