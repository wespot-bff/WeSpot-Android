package com.bff.wespot.state.featureoverview

import com.bff.wespot.model.dynamicui.FeatureOverview

data class FeatureOverviewUiState(
    val ui: FeatureOverview = FeatureOverview(),
    val isLoading: Boolean = false,
)
