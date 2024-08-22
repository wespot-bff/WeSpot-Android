package com.bff.wespot.data.remote.model.auth.request

import kotlinx.serialization.Serializable

@Serializable
data class ConsentsDto (
    val marketing: Boolean,
)