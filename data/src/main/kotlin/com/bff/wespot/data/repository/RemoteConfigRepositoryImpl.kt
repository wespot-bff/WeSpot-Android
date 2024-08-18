package com.bff.wespot.data.repository

import com.bff.wespot.data.remote.config.RemoteConfigDataSource
import com.bff.wespot.domain.repository.RemoteConfigRepository
import javax.inject.Inject

class RemoteConfigRepositoryImpl @Inject constructor(
    private val remoteConfigDataSource: RemoteConfigDataSource
) : RemoteConfigRepository {
    override suspend fun startRemoteConfig(): Boolean =
        remoteConfigDataSource.startRemoteConfig()

    override fun fetchFromRemoteConfig(key: String): String =
        remoteConfigDataSource.fetchFromRemoteConfig(key)
}