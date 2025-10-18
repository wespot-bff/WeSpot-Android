package com.bff.wespot.analytics

data class AnalyticsEvent(
    val name: String,
    val params: List<Param> = emptyList()
) {
    data class Param(val key: String, val value: String)
}
