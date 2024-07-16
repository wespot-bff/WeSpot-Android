package com.bff.wespot.network.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class SchoolListDto(
    val schools: List<SchoolDto>
)