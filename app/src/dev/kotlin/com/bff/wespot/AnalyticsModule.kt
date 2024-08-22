package com.bff.wespot

import com.bff.wespot.analytic.AnalyticsHelper
import com.bff.wespot.analytic.DebugAnalyticsHelper
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
    fun provideAnalyticsHelper(): AnalyticsHelper {
        return DebugAnalyticsHelper()
    }
}