package com.bff.wespot.domain.repository.serverDriven

import com.bff.wespot.model.notification.NotificationType
import com.bff.wespot.model.serverDriven.overview.UpdateOverview

interface UpdateOverviewRepository {
    suspend fun getUpdateOverview(notificationType: NotificationType): Result<UpdateOverview>
}
