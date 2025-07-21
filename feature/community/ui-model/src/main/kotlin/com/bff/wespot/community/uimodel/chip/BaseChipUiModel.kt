package com.bff.wespot.community.uimodel.chip

import com.bff.wespot.model.community.chip.BaseChip
import com.bff.wespot.model.community.chip.FilterChip

sealed interface BaseChipUiModel

fun BaseChip.toUiModel(): BaseChipUiModel = when (this) {
    is FilterChip -> this.toUiModel()
}
