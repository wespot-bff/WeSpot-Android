package com.bff.wespot.data.remote.model.message.response

import com.bff.wespot.model.message.response.MessageProfile
import kotlinx.serialization.Serializable

@Serializable
data class MessageProfileDto(
    val isAnonymous: Boolean,
    val iconUrl: String,
    val name: String,
    val schoolName: String = "",
    val grade: Int = -1,
    val classNumber: Int = -1,
) {
    fun toMessageProfile(): MessageProfile = MessageProfile(
        isAnonymous = isAnonymous,
        iconUrl = iconUrl,
        name = name,
        schoolName = schoolName,
        grade = grade,
        classNumber = classNumber,
    )
}
