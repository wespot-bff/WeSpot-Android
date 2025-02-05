package com.bff.wespot.model.serverDriven.overview

import com.bff.wespot.model.serverDriven.BaseComponent

data class UpdateOverview(
    val id: Int = -1,
    val name: String = "",
    val data: List<BaseComponent> = listOf(),
)
