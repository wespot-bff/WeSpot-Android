package com.bff.wespot.analytic

interface AnalyticsHelper {
    fun logEvent(event: AnalyticsEvent)
    fun updateUserId(userId: String)
}