package com.bff.wespot.data.repository.user

import com.bff.wespot.data.local.source.ProfileDataSource
import com.bff.wespot.domain.repository.user.ProfileRepository
import com.bff.wespot.model.user.response.Profile
import com.bff.wespot.model.user.response.ProfileCharacter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileDataSource: ProfileDataSource,
): ProfileRepository {
    override val profileDataFlow: Flow<Profile> = profileDataSource.profileDataFlow

    override suspend fun getProfile(): Profile = profileDataSource.getProfile()

    override suspend fun setProfile(profile: Profile) =
        profileDataSource.setProfile(profile)

    override suspend fun updateIntroduction(introduction: String) =
        profileDataSource.updateIntroduction(introduction)

    override suspend fun updateProfileCharacter(profileCharacter: ProfileCharacter) =
        profileDataSource.updateProfileCharacter(profileCharacter)
}
