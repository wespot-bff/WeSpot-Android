package com.bff.wespot.data.mapper.auth

import com.bff.wespot.data.remote.model.auth.request.ConsentsDto
import com.bff.wespot.data.remote.model.auth.request.SignInDto
import com.bff.wespot.data.remote.model.auth.request.SignUpDto
import com.bff.wespot.model.auth.request.SignIn
import com.bff.wespot.model.auth.request.SignUp
import com.bff.wespot.model.auth.response.Consents

internal fun SignIn.toDto(fcmToken: String) =
    SignInDto(
        socialType = socialType,
        identityToken = accessToken,
        versionName = versionName,
        fcmToken = fcmToken,
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
        versionName = versionName,
        consents = consents.toDto(),
        profileUrl = profileUrl,
        introduction = introduction,
    )