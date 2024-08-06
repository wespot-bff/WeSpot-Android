package com.bff.wespot.data.remote.source.user

import com.bff.wespot.data.remote.model.user.request.IntroductionDto
import com.bff.wespot.data.remote.model.user.response.UserListDto
import com.bff.wespot.data.remote.model.user.response.ProfileDto

interface UserDataSource {
    suspend fun getUserListByName(name: String, cursorId: Int): Result<UserListDto>

    suspend fun getProfile(): Result<ProfileDto>

    suspend fun updateIntroduction(introduction: IntroductionDto): Result<Unit>
}
