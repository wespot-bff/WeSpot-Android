package com.bff.wespot.model.serverDriven

import com.bff.wespot.model.serverDriven.section.BaseSection

data class OnBoarding(
    val id: Int,
    val name: String,
    val data: List<BaseSection>,
)
