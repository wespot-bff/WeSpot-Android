package com.bff.wespot.domain.repository.user

import com.bff.wespot.model.user.response.Profile
import com.bff.wespot.model.user.response.User

interface UserRepository {
    suspend fun getUserListByName(name: String): Result<List<User>>

    suspend fun getProfile(): Result<Profile>
}
