package com.bff.wespot.data.local.source

import androidx.datastore.core.DataStore
import com.bff.wespot.data.local.ProfilePreference
import com.bff.wespot.data.local.common.mapper.toProfile
import com.bff.wespot.data.local.copy
import com.bff.wespot.model.user.response.Profile
import com.bff.wespot.model.user.response.ProfileCharacter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class ProfileDataSourceImpl @Inject constructor(
    private val preference: DataStore<ProfilePreference>
): ProfileDataSource {
    override val profileDataFlow: Flow<Profile> = preference.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.e(exception)
                emit(ProfilePreference.getDefaultInstance())
            } else {
                throw exception
            }
        }.map {
            it.toProfile()
        }

    override suspend fun getProfile(): Profile = profileDataFlow.first()

    override suspend fun setProfile(profile: Profile) {
        preference.updateData {
            val profileCharacter = it.profileCharacter.toBuilder()
                .setIconUrl(profile.profileCharacter.iconUrl)
                .setBackgroundColor(profile.profileCharacter.backgroundColor)
                .build()

            it.toBuilder()
                .setId(profile.id)
                .setName(profile.name)
                .setSchoolName(profile.schoolName)
                .setGrade(profile.grade)
                .setClassNumber(profile.classNumber)
                .setGender(profile.gender)
                .setIntroduction(profile.introduction)
                .setProfileCharacter(profileCharacter)
                .build()
        }
    }

    override suspend fun updateIntroduction(introduction: String) {
        preference.updateData {
            it.copy {
                this.introduction = introduction
            }
        }
    }

    override suspend fun updateProfileCharacter(profileCharacter: ProfileCharacter) {
        preference.updateData {
            it.copy {
                this.profileCharacter = it.profileCharacter.toBuilder()
                    .setIconUrl(profileCharacter.iconUrl)
                    .setBackgroundColor(profileCharacter.backgroundColor)
                    .build()
            }
        }
    }

    override suspend fun clearProfile() {
        preference.updateData {
            it.toBuilder().clear().build()
        }
    }
}
