package com.bff.wespot

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class WeSpotApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)
    }
}