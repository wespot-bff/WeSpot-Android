package com.bff.wespot.model.auth.request

import com.bff.wespot.model.auth.response.Consents

data class SignUp(
    val name: String,
    val schoolId: Int,
    val grade: Int,
    val classNumber: Int,
    val gender: String,
    val consents: Consents
)
