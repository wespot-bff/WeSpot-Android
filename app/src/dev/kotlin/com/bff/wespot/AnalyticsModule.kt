package com.bff.wespot

import com.bff.wespot.analytic.AnalyticsHelper
import com.bff.wespot.analytic.DebugAnalyticsHelper
import com.bff.wespot.domain.repository.DataStoreRepository
import com.bff.wespot.domain.util.DataStoreKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {
    @Provides
    @Singleton
    fun provideAnalyticsHelper(
        repository: DataStoreRepository
    ): AnalyticsHelper = runBlocking {
        val userId = repository.getString(DataStoreKey.ID).first()
        DebugAnalyticsHelper(userId)
    }
}