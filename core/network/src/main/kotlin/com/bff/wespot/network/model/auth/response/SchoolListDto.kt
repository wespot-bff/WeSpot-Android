package com.bff.wespot.network.model.auth.response

import kotlinx.serialization.Serializable

@Serializable
data class SchoolListDto(
    val schools: List<SchoolDto>
)