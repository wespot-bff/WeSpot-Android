package com.bff.wespot

import com.bff.wespot.analytic.AnalyticsEvent
import com.bff.wespot.analytic.AnalyticsEvent.Param
import com.bff.wespot.analytic.AnalyticsHelper
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import javax.inject.Inject

class FirebaseAnalyticsHelper @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : AnalyticsHelper {
    override fun logEvent(event: AnalyticsEvent) {
        firebaseAnalytics.logEvent(event.type) {
            event.extras.forEach { (key, value) ->
                Param(
                    key = key.take(40),
                    value = value.take(100),
                )
            }
        }
    }
}