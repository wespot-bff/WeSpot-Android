package com.bff.wespot.data.mapper.auth

import com.bff.wespot.data.remote.model.auth.request.ConsentsDto
import com.bff.wespot.data.remote.model.auth.request.SignUpDto
import com.bff.wespot.model.auth.request.KakaoAuthToken
import com.bff.wespot.model.auth.request.SignUp
import com.bff.wespot.model.auth.response.Consents

internal fun KakaoAuthToken.toDto() =
    com.bff.wespot.data.remote.model.auth.request.KakaoAuthTokenDto(
        socialType = socialType,
        identityToken = idToken ?: "",
    )

internal fun Consents.toDto() =
    ConsentsDto(
        marketing = marketing
    )

internal fun SignUp.toDto(token: String) =
    SignUpDto(
        name = name,
        schoolId = schoolId,
        grade = grade,
        classNumber = classNumber,
        gender = gender,
        signUpToken = token,
        consents = consents.toDto()
    )