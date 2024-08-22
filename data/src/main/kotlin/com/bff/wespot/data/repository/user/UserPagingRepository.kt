package com.bff.wespot.data.repository.user

import com.bff.wespot.data.paging.user.UserPagingSource
import com.bff.wespot.data.remote.source.user.UserDataSource
import com.bff.wespot.domain.paging.BasePagingSource
import com.bff.wespot.domain.repository.BasePagingRepository
import com.bff.wespot.model.common.Paging
import com.bff.wespot.model.user.response.User
import javax.inject.Inject

class UserPagingRepository @Inject constructor(
    private val userDataSource: UserDataSource,
) : BasePagingRepository<User, Paging<User>>() {
    override fun pagingSource(
        parameter: Map<String, String>?,
    ): BasePagingSource<User, Paging<User>> =
        UserPagingSource(userDataSource, parameter?.get("name"))
}
