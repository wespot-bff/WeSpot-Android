package com.bff.wespot.data.remote.model.serverDriven.section

import com.bff.wespot.model.serverDriven.section.BaseSection
import kotlinx.serialization.Serializable

@Serializable
sealed interface BaseSectionDto {
    fun toDomain(): BaseSection
}