package com.bff.wespot.data.mapper.auth

import com.bff.wespot.model.auth.request.KakaoAuthToken
import com.bff.wespot.network.model.auth.request.KakaoAuthTokenDto

internal fun KakaoAuthToken.toDto() =
    KakaoAuthTokenDto(
        socialType = socialType,
        identityToken = idToken ?: "",
    )