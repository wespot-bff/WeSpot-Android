package com.bff.wespot.domain.repository.dynamicui

import com.bff.wespot.model.dynamicui.FeatureOverview
import com.bff.wespot.model.notification.NotificationType

interface DynamicUiRepository {
    suspend fun getFeatureOverview(notificationType: NotificationType): Result<FeatureOverview>
}
