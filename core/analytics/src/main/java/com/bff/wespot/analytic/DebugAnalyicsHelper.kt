package com.bff.wespot.analytic

import timber.log.Timber
import javax.inject.Inject

class DebugAnalyticsHelper @Inject constructor(
    private var userId: String = "",
) : AnalyticsHelper {
    override fun logEvent(event: AnalyticsEvent) {
        Timber.i("Event: $userId, $event")
    }

    override fun updateUserId(userId: String) {
        this.userId = userId
    }
}