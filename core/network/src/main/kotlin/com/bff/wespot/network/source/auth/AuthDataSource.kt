package com.bff.wespot.network.source.auth

import com.bff.wespot.network.model.auth.SchoolListDto

interface AuthDataSource {
    suspend fun getSchoolList(search: String): Result<SchoolListDto>
}