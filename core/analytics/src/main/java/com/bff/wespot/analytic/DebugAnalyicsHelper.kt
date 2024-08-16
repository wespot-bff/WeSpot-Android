package com.bff.wespot.analytic

import timber.log.Timber
import javax.inject.Inject

class DebugAnalyticsHelper @Inject constructor() :AnalyticsHelper {
    override fun logEvent(event: AnalyticsEvent) {
        Timber.i("Event: $event")
    }
}