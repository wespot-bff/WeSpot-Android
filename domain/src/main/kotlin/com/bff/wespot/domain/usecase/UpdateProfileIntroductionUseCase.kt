package com.bff.wespot.domain.usecase

import com.bff.wespot.domain.repository.user.ProfileRepository
import com.bff.wespot.domain.repository.user.UserRepository
import javax.inject.Inject

class UpdateProfileIntroductionUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(introduction: String): Result<Unit> =
        userRepository.updateIntroduction(introduction).onSuccess {
            runCatching {
                profileRepository.updateIntroduction(introduction)
            }.onFailure { exception ->
                return Result.failure(exception)
            }
        }
}
