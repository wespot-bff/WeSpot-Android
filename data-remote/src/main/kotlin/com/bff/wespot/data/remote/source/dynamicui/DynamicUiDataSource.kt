package com.bff.wespot.data.remote.source.dynamicui

import com.bff.wespot.data.remote.model.dynamicui.FeatureOverviewDto

interface DynamicUiDataSource {
    suspend fun getFeatureOverview(notificationType: String): Result<FeatureOverviewDto>
}
