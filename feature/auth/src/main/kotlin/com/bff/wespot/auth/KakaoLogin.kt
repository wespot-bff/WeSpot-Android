package com.bff.wespot.auth

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class KakaoLoginManager @Inject constructor(
    @ActivityContext private val context: Context
) {

    suspend fun loginWithKakao(): OAuthToken {
        val loginState = getKakaoLoginState()

        return when (loginState) {
            KaKaoLoginState.KAKAO_TALK_LOGIN -> {
                try {
                    UserApiClient.loginWithKakaoTalk()
                } catch (error: Throwable) {
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        throw error
                    }

                    UserApiClient.loginWithKakaoAccount()
                }
            }

            KaKaoLoginState.KAKAO_ACCOUNT_LOGIN -> {
                UserApiClient.loginWithKakaoAccount()
            }
        }
    }


    private suspend fun UserApiClient.Companion.loginWithKakaoTalk(): OAuthToken {
        return suspendCoroutine { continuation ->
            instance.loginWithKakaoTalk(context) { token, error ->
                continuation.resumeTokenOrException(token, error)
            }
        }
    }

    private suspend fun UserApiClient.Companion.loginWithKakaoAccount(): OAuthToken {
        return suspendCoroutine { continuation ->
            instance.loginWithKakaoAccount(context) { token, error ->
                continuation.resumeTokenOrException(token, error)
            }
        }
    }

    private fun getKakaoLoginState(): KaKaoLoginState =
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            KaKaoLoginState.KAKAO_TALK_LOGIN
        } else {
            KaKaoLoginState.KAKAO_ACCOUNT_LOGIN
        }

    private fun Continuation<OAuthToken>.resumeTokenOrException(
        token: OAuthToken?,
        error: Throwable?
    ) {
        if (error != null) {
            resumeWithException(error)
        } else if (token != null) {
            resume(token)
        } else {
            resumeWithException(RuntimeException("Failed to get kakao access token, reason is not clear."))
        }
    }
}

enum class KaKaoLoginState {
    KAKAO_TALK_LOGIN, KAKAO_ACCOUNT_LOGIN
}