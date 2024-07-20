package com.bff.wespot.data.remote.model.auth.response

import kotlinx.serialization.Serializable

@Serializable
data class SchoolListDto(
    val schools: List<SchoolDto>
)