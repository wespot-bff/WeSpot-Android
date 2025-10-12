package com.bff.wespot.analytics

data class AnalyticsEvent(
    val name: String,
    val params: List<Param> = emptyList()
) {
    data class Param(val key: String, val value: String)

    object ParamKeys {
        const val SERVICE_NAME = "service_name"
        const val SCREEN_NAME = "screen_name"
        const val AREA = "area"
        const val VERSION = "version"
    }
}
