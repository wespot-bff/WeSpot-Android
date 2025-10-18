package com.bff.wespot.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.staticCompositionLocalOf
import com.bff.wespot.analytics.AnalyticsEvent.Param

val LocalAnalyticsHelper = staticCompositionLocalOf<AnalyticsHelper> {
    NoOpAnalyticsHelper()
}

fun AnalyticsHelper.logScreenView(
    name: String,
    extras: List<Param> = emptyList(),
) {
    val params = buildList {
        addAll(extras)
    }

    logEvent(AnalyticsEvent("view_$name", params))
}

fun AnalyticsHelper.logImpression(
    name: String,
    extras: List<Param> = emptyList(),
) {
    val params = buildList {
        addAll(extras)
    }

    logEvent(AnalyticsEvent("impression_$name", params))
}

fun AnalyticsHelper.logClick(
    name: String,
    area: String = "",
    extras: List<Param> = listOf(),
) {
    val params = buildList {
        add(Param("area", area))
        addAll(extras)
    }

    logEvent(AnalyticsEvent("click_$name", params))
}

@Composable
fun TrackScreenViewEvent(
    name: String,
    extras: List<Param> = emptyList(),
    analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current,
) = DisposableEffect(Unit) {
    analyticsHelper.logScreenView(
        name = name,
        extras = extras,
    )
    onDispose {}
}
