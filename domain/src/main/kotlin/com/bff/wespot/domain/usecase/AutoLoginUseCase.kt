package com.bff.wespot.domain.usecase

import com.bff.wespot.domain.repository.DataStoreRepository
import com.bff.wespot.domain.repository.RemoteConfigRepository
import com.bff.wespot.domain.util.DataStoreKey
import com.bff.wespot.domain.util.RemoteConfigKey
import com.bff.wespot.model.constants.LoginState
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class AutoLoginUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val remoteConfigRepository: RemoteConfigRepository,
) {
    suspend operator fun invoke(versionCode: String): LoginState {
        val minVersion = remoteConfigRepository.fetchFromRemoteConfig(RemoteConfigKey.MIN_VERSION)

        if (versionCompare(minVersion, versionCode)) {
            return LoginState.FORCE_UPDATE
        }

        dataStoreRepository.getString(DataStoreKey.REFRESH_TOKEN_EXPIRED_AT).first().let {
            if (it.isEmpty()) {
                return LoginState.LOGIN_FAILURE
            }
            val dateTime = LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME)
            val now = LocalDateTime.now()

            return if (it.isEmpty() || dateTime.isBefore(now)) {
                LoginState.LOGIN_FAILURE
            } else {
                LoginState.LOGIN_SUCCESS
            }
        }
    }

    private fun versionCompare(minVersion: String, appVersion: String): Boolean {
        val minVersionSplit = minVersion.split(".").map { it.toInt() }
        val appVersionSplit = appVersion.split(".").map { it.toInt() }

        for (i in 0 until minOf(minVersionSplit.size, appVersionSplit.size)) {
            if (minVersionSplit[i] > appVersionSplit[i]) {
                return true
            } else if (minVersionSplit[i] == appVersionSplit[i]) {
                continue
            } else {
                return false
            }
        }

        return minVersionSplit.size > appVersionSplit.size
    }
}
