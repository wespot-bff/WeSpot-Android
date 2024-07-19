package com.bff.wespot.domain.usecase

import com.bff.wespot.domain.repository.DataStoreRepository
import com.bff.wespot.domain.util.DataStoreKey
import com.bff.wespot.model.constants.LoginState
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class AutoLoginUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
) {
    suspend operator fun invoke(): LoginState =
        dataStoreRepository.getString(DataStoreKey.REFRESH_TOKEN_EXPIRED_AT).first().let {
            val dateTime = LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME)
            val now = LocalDateTime.now()

            return if (it.isEmpty() || dateTime.isBefore(now)) {
                LoginState.LOGIN_FAILURE
            } else {
                LoginState.LOGIN_SUCCESS
            }
        }
}
