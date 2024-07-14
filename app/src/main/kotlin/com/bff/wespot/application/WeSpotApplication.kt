package com.bff.wespot.application

import android.app.Application
import com.bff.wespot.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class WeSpotApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initialTimber()
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
                    /*if(t != null){
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
