package com.bff.wespot.data.repository.serverDriven

import com.bff.wespot.data.remote.source.serverDriven.UpdateOverviewDataSource
import com.bff.wespot.domain.repository.serverDriven.UpdateOverviewRepository
import com.bff.wespot.model.notification.NotificationType
import com.bff.wespot.model.serverDriven.overview.UpdateOverview
import javax.inject.Inject

class UpdateOverviewRepositoryImpl @Inject constructor(
    private val dataSource: UpdateOverviewDataSource,
): UpdateOverviewRepository {
    override suspend fun getUpdateOverview(
        notificationType: NotificationType,
    ): Result<UpdateOverview> =
        dataSource.getUpdateOverview(notificationType.name).mapCatching { response ->
            response.toUpdateOverview()
        }
}
