package com.bff.wespot.model.auth.request

import kotlinx.serialization.Serializable

@Serializable
data class RevokeReasonListDto(
    val revokeReasons: List<String>,
)
