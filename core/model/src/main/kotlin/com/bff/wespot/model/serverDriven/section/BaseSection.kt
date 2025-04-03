package com.bff.wespot.model.serverDriven.section

import com.bff.wespot.model.serverDriven.BaseComponent

interface BaseSection {
    val components: List<BaseComponent>
}
