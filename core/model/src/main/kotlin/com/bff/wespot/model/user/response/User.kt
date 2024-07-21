package com.bff.wespot.model.user.response

data class User(
    val id: Int,
    val name: String,
    val schoolName: String,
    val grade: Int,
    val group: Int,
) {
    fun toDescription() = "${schoolName.removeSuffix("학교")} ${grade}학년 ${group}반 $name"
}
