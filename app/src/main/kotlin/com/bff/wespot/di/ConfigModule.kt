package com.bff.wespot.di

import android.content.Context
import com.bff.wespot.R
import com.bff.wespot.network.NetworkStateChecker
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConfigModule {
    @Provides
    @Singleton
    fun provideRemoteConfig(): FirebaseRemoteConfig {
        val remoteConfig = FirebaseRemoteConfig.getInstance().apply {
            val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build()
            setConfigSettingsAsync(configSettings)
            setDefaultsAsync(R.xml.remote_default_config)
        }

        return remoteConfig
    }

    @Provides
    @Singleton
    fun provideNetworkStateChecker(
        @ApplicationContext context: Context,
    ): NetworkStateChecker {
        return NetworkStateChecker(context)
    }

    @Provides
    @Singleton
    fun providesCrashlytics(): FirebaseCrashlytics = Firebase.crashlytics

    @Provides
    @Singleton
    fun provideFirebaseMessaging(): FirebaseMessaging = Firebase.messaging
}
