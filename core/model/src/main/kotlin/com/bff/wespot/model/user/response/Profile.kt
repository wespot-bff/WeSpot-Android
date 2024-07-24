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
    fun toDescription() =
        "${schoolName.replace("중학교", "중").replace("고등학교", "고")} ${grade}학년 ${classNumber}반 $name"
}
