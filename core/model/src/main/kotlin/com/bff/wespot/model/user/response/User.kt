package com.bff.wespot.model.user.response

data class User(
    val id: Int,
    val name: String,
    val grade: Int,
    val classNumber: Int,
    val schoolName: String,
    val profileCharacter: ProfileCharacter,
) {
    constructor() : this(-1, "", -1, -1, "", ProfileCharacter())

    fun toSchoolInfo() = "$schoolName ${grade}학년 ${classNumber}반"

    fun toShortSchoolName(): String =
        schoolName.replace("중학교", "중").replace("고등학교", "고")

    fun toUserInfoWithoutSchoolName() = "${grade}학년 ${classNumber}반 $name"

    fun toDescription(): String {
        val schoolName = schoolName.replace("중학교", "중").replace("고등학교", "고")
        return "$schoolName ${grade}학년 ${classNumber}반 $name"
    }
}
