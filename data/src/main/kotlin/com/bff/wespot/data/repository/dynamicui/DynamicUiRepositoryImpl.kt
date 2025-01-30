package com.bff.wespot.data.repository.dynamicui

import com.bff.wespot.data.remote.source.dynamicui.DynamicUiDataSource
import com.bff.wespot.domain.repository.dynamicui.DynamicUiRepository
import com.bff.wespot.model.dynamicui.FeatureOverview
import com.bff.wespot.model.notification.NotificationType
import javax.inject.Inject

class DynamicUiRepositoryImpl @Inject constructor(
    private val dynamicUiDataSource: DynamicUiDataSource,
): DynamicUiRepository {
    override suspend fun getFeatureOverview(
        notificationType: NotificationType,
    ): Result<FeatureOverview> =
        dynamicUiDataSource.getFeatureOverview(notificationType.name).mapCatching { response ->
            response.toFeatureOverview()
        }
}
