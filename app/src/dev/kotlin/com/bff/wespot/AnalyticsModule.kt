package com.bff.wespot

import com.bff.wespot.analytics.AnalyticsHelper
import com.bff.wespot.analytics.DebugAnalyticsHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {
    @Provides
    @Singleton
    fun provideAnalyticsHelper(): AnalyticsHelper = DebugAnalyticsHelper()
}
