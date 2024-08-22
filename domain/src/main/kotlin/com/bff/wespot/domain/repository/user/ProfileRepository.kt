package com.bff.wespot.domain.repository.user

import com.bff.wespot.model.user.response.Profile
import com.bff.wespot.model.user.response.ProfileCharacter
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    val profileDataFlow: Flow<Profile>

    suspend fun getProfile(): Profile

    suspend fun setProfile(profile: Profile)

    suspend fun updateIntroduction(introduction: String)

    suspend fun updateProfileCharacter(profileCharacter: ProfileCharacter)

    suspend fun clearProfile()
}
