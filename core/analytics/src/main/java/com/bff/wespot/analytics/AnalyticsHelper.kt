package com.bff.wespot.analytics

interface AnalyticsHelper {
    fun logEvent(event: AnalyticsEvent)
    fun updateUserId(userId: String)
}