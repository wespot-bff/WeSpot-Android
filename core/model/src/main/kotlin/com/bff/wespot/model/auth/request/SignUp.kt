package com.bff.wespot.model.auth.request

data class SignUp(
    val schoolId: Int,
    val grade: Int,
    val group: Int,
    val gender: String,
)
