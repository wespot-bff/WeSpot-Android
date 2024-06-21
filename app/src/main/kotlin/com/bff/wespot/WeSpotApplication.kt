package com.bff.wespot

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class WeSpotApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val key = BuildConfig.KAKAO_APP_KEY
        KakaoSdk.init(this, key)
    }
}