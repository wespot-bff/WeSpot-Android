package com.bff.wespot.domain.usecase

import com.bff.wespot.domain.repository.user.ProfileRepository
import com.bff.wespot.domain.repository.user.UserRepository
import javax.inject.Inject

class CacheProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(): Result<Unit> = userRepository.getProfile().mapCatching {
        runCatching {
            profileRepository.setProfile(it)
        }.onFailure { exception ->
            return Result.failure(exception)
        }
    }
}
