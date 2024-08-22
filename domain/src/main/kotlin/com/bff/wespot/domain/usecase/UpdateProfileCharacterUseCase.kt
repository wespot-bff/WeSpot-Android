package com.bff.wespot.domain.usecase

import com.bff.wespot.domain.repository.user.ProfileRepository
import com.bff.wespot.domain.repository.user.UserRepository
import com.bff.wespot.model.user.response.ProfileCharacter
import javax.inject.Inject

class UpdateProfileCharacterUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(character: ProfileCharacter): Result<Unit> =
        userRepository.updateCharacter(character).onSuccess {
            runCatching {
                profileRepository.updateProfileCharacter(character)
            }.onFailure { exception ->
                return Result.failure(exception)
            }
        }
}
