package com.bff.wespot.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.staticCompositionLocalOf
import com.bff.wespot.analytics.AnalyticsEvent.Param
import com.bff.wespot.analytics.AnalyticsEvent.ParamKeys
import com.bff.wespot.analytics.params.AnalyticsArea
import com.bff.wespot.analytics.params.AnalyticsService

val LocalAnalyticsHelper = staticCompositionLocalOf<AnalyticsHelper> {
    NoOpAnalyticsHelper()
}

fun AnalyticsHelper.logScreenView(
    name: String,
    service: AnalyticsService,
    version: String = "v1",
    extras: List<Param> = emptyList(),
) {
    val params = buildList {
        addAll(extras)
        add(Param(ParamKeys.SERVICE_NAME, service.value))
        add(Param(ParamKeys.VERSION, version))
    }

    logEvent(AnalyticsEvent(name, params))
}

fun AnalyticsHelper.logClickAction(
    name: String,
    service: AnalyticsService,
    screen: String,
    area: AnalyticsArea,
    version: String = "v1",
    extras: List<Param> = listOf(),
) {
    val params = buildList {
        addAll(extras)
        add(Param(ParamKeys.SERVICE_NAME, service.value))
        add(Param(ParamKeys.SCREEN_NAME, screen))
        add(Param(ParamKeys.AREA, area.value))
        add(Param(ParamKeys.VERSION, version))
    }

    logEvent(AnalyticsEvent(name, params))
}

@Composable
fun TrackScreenViewEvent(
    name: String,
    service: AnalyticsService,
    version: String = "v1",
    extras: List<Param> = emptyList(),
    analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current,
) = DisposableEffect(Unit) {
    analyticsHelper.logScreenView(
        name = name,
        service = service,
        version = version,
        extras = extras,
    )
    onDispose {}
}
