package com.bff.wespot.server.driven.overview

import com.bff.wespot.model.serverDriven.BaseComponent

data class UpdateOverviewUiState(
    val contents: List<BaseComponent> = listOf(),
    val isLoading: Boolean = false,
)
