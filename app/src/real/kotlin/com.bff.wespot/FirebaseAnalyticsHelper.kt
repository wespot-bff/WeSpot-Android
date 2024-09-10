package com.bff.wespot

import com.bff.wespot.analytic.AnalyticsEvent
import com.bff.wespot.analytic.AnalyticsHelper
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import javax.inject.Inject

class FirebaseAnalyticsHelper @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : AnalyticsHelper {
    private var userId: String = ""

    override fun logEvent(event: AnalyticsEvent) {
        firebaseAnalytics.logEvent(event.type) {
            param("userId", userId)
            event.extras.forEach { (key, value) ->
                param(
                    key = key.take(40),
                    value = value.take(100),
                )
            }
        }
    }

    override fun updateUserId(userId: String) {
        this.userId = userId
    }
}