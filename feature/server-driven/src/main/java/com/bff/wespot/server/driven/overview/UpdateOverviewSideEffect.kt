package com.bff.wespot.server.driven.overview

sealed interface UpdateOverviewSideEffect {
    data object DismissDialog : UpdateOverviewSideEffect
    data class NavigateDeepLink(val deepLink: String) : UpdateOverviewSideEffect
}
