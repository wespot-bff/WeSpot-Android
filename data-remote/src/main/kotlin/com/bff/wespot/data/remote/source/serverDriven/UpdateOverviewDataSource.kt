package com.bff.wespot.data.remote.source.serverDriven

import com.bff.wespot.data.remote.model.serverDriven.overview.UpdateOverviewDto

interface UpdateOverviewDataSource {
    suspend fun getUpdateOverview(notificationType: String): Result<UpdateOverviewDto>
}
