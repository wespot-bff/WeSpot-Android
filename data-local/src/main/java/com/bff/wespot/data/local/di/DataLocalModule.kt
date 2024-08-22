package com.bff.wespot.data.local.di

import com.bff.wespot.data.local.source.ProfileDataSource
import com.bff.wespot.data.local.source.ProfileDataSourceImpl
import com.bff.wespot.data.local.source.WeSpotDataStore
import com.bff.wespot.data.local.source.WeSpotDataStoreImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataLocalModule {
    @Binds
    @Singleton
    fun bindsWeSpotDataStore(
        weSpotDataStoreImpl: WeSpotDataStoreImpl
    ): WeSpotDataStore

    @Binds
    @Singleton
    fun bindsProfileDataSource(
        profileDataSourceImpl: ProfileDataSourceImpl
    ): ProfileDataSource
}