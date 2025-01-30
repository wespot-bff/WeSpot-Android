package com.bff.wespot.state.featureoverview

sealed interface FeatureOverviewSideEffect {
    data object DismissDialog : FeatureOverviewSideEffect
    data class NavigateScreen(val deepLink: String) : FeatureOverviewSideEffect
}
