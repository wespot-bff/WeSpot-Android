package com.bff.wespot.application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.bff.wespot.BuildConfig
import com.bff.wespot.common.CHANNEL_DESCRIPTION
import com.bff.wespot.common.CHANNEL_ID
import com.bff.wespot.common.CHANNEL_NAME
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class WeSpotApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initialTimber()
        initKakaoSdk()
        initNotificationChannel()
    }

    private fun initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
            }

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun initKakaoSdk() {
        val key = BuildConfig.KAKAO_APP_KEY
        KakaoSdk.init(this, key)
    }

    private fun initialTimber() {
        if (BuildConfig.DEBUG) {
            plantDebugTimberTree()
        } else {
            plantReleaseTimberTree()
        }
    }

    private fun plantDebugTimberTree() {
        Timber.plant(Timber.DebugTree())
    }

    private fun plantReleaseTimberTree() {
        Timber.plant(
            object : Timber.Tree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    /* TODO Release App Log Crashlytics 연동
                    if (t != null) {
                        if(priority == Log.ERROR){
                            Crashlytics.logError(priority, tag, message)
                        }
                        else if(priority == Log.WARN){
                            Crashlytics.logWarning(priority, tag, message)
                        }
                    }*/
                }
            },
        )
    }
}
