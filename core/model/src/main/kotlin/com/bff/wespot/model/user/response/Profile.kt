package com.bff.wespot.model.user.response

data class Profile(
    val id: Int,
    val name: String,
    val schoolName: String,
    val grade: Int,
    val group: Int,
    val gender: String,
    val introduction: String,
    val profileCharacter: ProfileCharacter,
) {
    fun toDescription() = "${schoolName.removeSuffix("학교")} ${grade}학년 ${group}반 $name"
}
