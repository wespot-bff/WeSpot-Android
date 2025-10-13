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
    val needToAnnounceAboutPolicy: Boolean,
) {
    constructor() : this(-1, "", "", -1, -1, "", "", ProfileCharacter(), false)

    fun toDescription(): String {
        val schoolName = schoolName.replace("중학교", "중").replace("고등학교", "고")
        return "$schoolName ${grade}학년 ${classNumber}반 $name"
    }

    fun toGenderKorean(): String = when (this.gender) {
        "FEMALE" -> "여"
        "MALE" -> "남"
        else -> ""
    }

    fun toMessageReceiverInfo(): String {
        val schoolName = schoolName.replace("중학교", "중").replace("고등학교", "고")
        return "$name | $schoolName ${grade}학년 ${classNumber}반"
    }

    fun toSchoolInfo() = "$schoolName ${grade}학년 ${classNumber}반"
}
