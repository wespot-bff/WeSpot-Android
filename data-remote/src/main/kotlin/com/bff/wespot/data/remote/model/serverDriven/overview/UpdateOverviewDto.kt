package com.bff.wespot.data.remote.model.serverDriven.overview

import com.bff.wespot.data.remote.model.serverDriven.BaseComponentDto
import com.bff.wespot.model.serverDriven.overview.UpdateOverview
import kotlinx.serialization.Serializable

@Serializable
data class UpdateOverviewDto(
    val id: Int,
    val name: String,
    val data: List<BaseComponentDto>,
) {
    fun toUpdateOverview() = UpdateOverview(
        id = id,
        name = name,
        data = data.map { it.toDomain() }
    )
}
