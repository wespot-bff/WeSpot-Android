package com.bff.wespot.model.user.response

data class Profile(
    val id: Int,
    val name: String,
    val schoolName: String,
    val grade: Int,
    val classNumber: Int,
    val gender: String,
    val introduction: String,
    val profileCharacter: ProfileCharacter,
) {
    constructor() : this(-1, "", "", -1, -1, "", "", ProfileCharacter())

    fun toDescription() = "${schoolName.removeSuffix("학교")} ${grade}학년 ${classNumber}반 $name"
}
