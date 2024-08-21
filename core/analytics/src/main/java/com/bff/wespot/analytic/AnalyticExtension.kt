package com.bff.wespot.analytic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.staticCompositionLocalOf
import com.bff.wespot.analytic.AnalyticsEvent.Param
import com.bff.wespot.analytic.AnalyticsEvent.ParamKeys
import com.bff.wespot.analytic.AnalyticsEvent.Types
import java.text.SimpleDateFormat

val LocalAnalyticsHelper = staticCompositionLocalOf<AnalyticsHelper> {
    NoOpAnalyticsHelper()
}

fun AnalyticsHelper.logScreenView(screenName: String, startTime: String, id: String?) {
    val param = mutableListOf(
        Param(ParamKeys.SCREEN_NAME, screenName),
        Param("start_time", startTime),
    )

    id?.let {
        param.add(Param("userId", id))
    }

    logEvent(
        AnalyticsEvent(
            type = Types.SCREEN_VIEW,
            extras = param,
        ),
    )
}

fun AnalyticsHelper.buttonClick(screenName: String, buttonId: String) {
    logEvent(
        AnalyticsEvent(
            type = Types.BUTTON_CLICK,
            extras = listOf(
                Param(ParamKeys.SCREEN_NAME, screenName),
                Param(ParamKeys.BUTTON_ID, buttonId),
            ),
        ),
    )
}

@Composable
fun TrackScreenViewEvent(
    screenName: String,
    id: String? = null,
    analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current,
) = DisposableEffect(Unit) {
    val time = System.currentTimeMillis()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    val startTime = dateFormat.format(time)
    analyticsHelper.logScreenView(screenName, startTime, id)
    onDispose {}
}