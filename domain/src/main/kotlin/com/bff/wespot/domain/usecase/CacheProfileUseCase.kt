package com.bff.wespot.domain.usecase

import com.bff.wespot.domain.repository.user.ProfileRepository
import com.bff.wespot.domain.repository.user.UserRepository
import com.bff.wespot.model.user.response.Profile
import javax.inject.Inject

class CacheProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(): Result<Profile> =
        userRepository.getProfile().mapCatching { profile ->
            runCatching {
                profileRepository.setProfile(profile)
            }.onFailure { exception ->
                throw exception
            }
            profile
        }
}
