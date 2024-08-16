package com.bff.wespot

import com.bff.wespot.analytic.AnalyticsHelper
import com.bff.wespot.analytic.DebugAnalyticsHelper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AnalyticsModule {
    @Binds
    @Singleton
    fun bindsAbstractAnalyticsHelper(helper: DebugAnalyticsHelper): AnalyticsHelper
}