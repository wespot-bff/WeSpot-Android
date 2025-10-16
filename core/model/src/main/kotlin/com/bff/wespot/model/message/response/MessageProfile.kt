package com.bff.wespot.model.message.response

data class MessageProfile(
    val isAnonymous: Boolean = false,
    val iconUrl: String = "",
    val name: String = "",
    val schoolName: String = "",
    val grade: Int = -1,
    val classNumber: Int = -1,
)
