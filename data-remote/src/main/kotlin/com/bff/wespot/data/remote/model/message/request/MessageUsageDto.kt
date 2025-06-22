package com.bff.wespot.data.remote.model.message.request

import kotlinx.serialization.Serializable

@Serializable
data class MessageUsageDto(
    val isEnableMessage: Boolean
)
