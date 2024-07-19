package com.bff.wespot.network.model.auth.request

import kotlinx.serialization.Serializable

@Serializable
data class ConsentsDto (
    val marketing: Boolean,
)