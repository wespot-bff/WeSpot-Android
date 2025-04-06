package com.bff.wespot.model.serverDriven.section

import com.bff.wespot.model.serverDriven.BaseComponent

data class BottomSection(
    override val components: List<BaseComponent>,
) : BaseSection
