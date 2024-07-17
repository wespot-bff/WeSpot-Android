package com.bff.wespot.data.mapper.auth

import com.bff.wespot.model.auth.request.KakaoAuthToken
import com.bff.wespot.model.auth.request.SignUp
import com.bff.wespot.network.model.auth.request.KakaoAuthTokenDto
import com.bff.wespot.network.model.auth.request.SignUpDto

internal fun KakaoAuthToken.toDto() =
    KakaoAuthTokenDto(
        socialType = socialType,
        identityToken = idToken ?: "",
    )

internal fun SignUp.toDto(token: String) =
    SignUpDto(
        schoolId = schoolId,
        grade = grade,
        group = group,
        gender = gender,
        userToken = token,
    )