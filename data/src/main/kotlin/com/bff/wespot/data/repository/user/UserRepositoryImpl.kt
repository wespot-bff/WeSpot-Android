package com.bff.wespot.data.repository.user

import com.bff.wespot.data.remote.source.user.UserDataSource
import com.bff.wespot.domain.repository.user.UserRepository
import com.bff.wespot.model.user.response.Profile
import com.bff.wespot.model.user.response.User
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
) : UserRepository {
    override suspend fun getUserListByName(name: String, cursorId: Int): Result<List<User>> =
        userDataSource.getUserListByName(name, cursorId).map { userListDto ->
            userListDto.users.map { userDto ->
                userDto.toUser()
            }
        }

    override suspend fun getProfile(): Result<Profile> =
        userDataSource.getProfile().map { profileDto ->
            profileDto.toProfile()
        }
}
