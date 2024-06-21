package com.bff.wespot.auth

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import kotlinx.coroutines.launch

@Composable
fun AuthScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val kakaoLoginManager = KakaoLoginManager(context)


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = {
            coroutineScope.launch {
                try {
                    val token = kakaoLoginManager.loginWithKakao()
                    Log.d("AUTHSCREEN", token.toString())
                } catch (e: Exception) {
                    if (e is ClientError && e.reason == ClientErrorCause.Cancelled) {
                        Log.d("AUTHSCREEN", "Login cancelled")
                    } else {
                        Log.e("AUTHSCREEN", "Login failed", e)
                    }
                }
            }
        }) {
            Text(text = "Login with Kakao")
        }
    }
}
