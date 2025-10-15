package com.bff.wespot.community.di

import androidx.lifecycle.SavedStateHandle
import com.bff.wespot.community.report.state.CommunityReportParams
import com.bff.wespot.community.report.state.ReportType
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object CommunityReportModule {
    @Provides
    fun provideCommunityReportParams(
        savedStateHandle: SavedStateHandle,
    ): CommunityReportParams {
        val targetId = savedStateHandle.get<String>("targetId") ?: ""
        val reportTypeString = savedStateHandle.get<String>("reportType") ?: "POST"
        val reportType = when (reportTypeString) {
            "COMMENT" -> ReportType.COMMENT
            else -> ReportType.POST
        }

        return CommunityReportParams(
            targetId = targetId,
            reportType = reportType,
        )
    }
}
