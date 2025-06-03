package com.bff.wespot.data.remote.model.message.response

import com.bff.wespot.data.remote.extensions.toISOLocalDateTime
import com.bff.wespot.model.message.response.SenderProfile
import kotlinx.serialization.Serializable

@Serializable
data class SenderProfileDto(
    val id: Int,
    val name: String,
    val image: String,
    val recentlyTalk: String? = null,
    val myTurnToAnswer: Boolean,
    val isAnonymous: Boolean,
) {
    fun toDomain() = SenderProfile(
        id = id,
        name = name,
        image = image,
        recentlyTalk = recentlyTalk?.toISOLocalDateTime(),
        myTurnToAnswer = myTurnToAnswer,
        isAnonymous = isAnonymous,
    )
}
