package com.bff.wespot.data.remote.model.community.chip

import com.bff.wespot.model.community.chip.BaseChip
import kotlinx.serialization.Serializable

@Serializable
sealed interface BaseChipDto {
    fun toDomain(): BaseChip
}