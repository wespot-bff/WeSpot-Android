package com.bff.wespot.data.remote.config

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.suspendCoroutine


@Singleton
class RemoteConfigDataSourceImpl @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) : RemoteConfigDataSource {
    override suspend fun startRemoteConfig(): Boolean {
        return suspendCoroutine { continuation ->
            remoteConfig.fetchAndActivate().addOnCompleteListener {
                continuation.resumeWith(Result.success(true))
            }.addOnFailureListener {
                Timber.e("fetchRemoteConfig: ", it)
            }
        }
    }

    override fun fetchFromRemoteConfig(key: String): String {
        return remoteConfig.getString(key)
    }
}