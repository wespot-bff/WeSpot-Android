package com.bff.wespot.domain.repository.auth

import com.bff.wespot.model.auth.School

interface AuthRepository {
    suspend fun getSchoolList(search: String): Result<List<School>>
}
