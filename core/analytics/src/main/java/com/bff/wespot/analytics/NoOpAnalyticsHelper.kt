package com.bff.wespot.analytics

class NoOpAnalyticsHelper : AnalyticsHelper {
    override fun logEvent(event: AnalyticsEvent) = Unit
    override fun updateUserId(userId: String) = Unit
}