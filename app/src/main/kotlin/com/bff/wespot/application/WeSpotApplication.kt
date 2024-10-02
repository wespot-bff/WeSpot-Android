package com.bff.wespot.application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.util.Log
import com.bff.wespot.BuildConfig
import com.bff.wespot.common.CHANNEL_DESCRIPTION
import com.bff.wespot.common.CHANNEL_ID
import com.bff.wespot.common.CHANNEL_NAME
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class WeSpotApplication : Application() {

    @Inject
    lateinit var crashlytics: FirebaseCrashlytics

    override fun onCreate() {
        super.onCreate()
        initialTimber()
        initKakaoSdk()
        initNotificationChannel()

        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
    }

    private fun initNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
            description = CHANNEL_DESCRIPTION
        }

        notificationManager.createNotificationChannel(channel)
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
        val releaseTree = object : Timber.Tree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                if (t != null) {
                    when (priority) {
                        Log.ERROR -> {
                            crashlytics.recordException(t)
                        }
                    }
                }
            }
        }

        Timber.plant(releaseTree)
    }
}
