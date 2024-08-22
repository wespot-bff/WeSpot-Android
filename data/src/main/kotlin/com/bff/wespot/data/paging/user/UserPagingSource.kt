package com.bff.wespot.data.paging.user

import com.bff.wespot.data.remote.source.user.UserDataSource
import com.bff.wespot.domain.paging.BasePagingSource
import com.bff.wespot.model.common.Paging
import com.bff.wespot.model.user.response.User
import com.bff.wespot.model.user.response.UserList

class UserPagingSource(
    private val userDataSource: UserDataSource,
    private val name: String?
): BasePagingSource<User, Paging<User>>() {
    override suspend fun fetchItems(cursorId: Int?): Paging<User> {
        name ?: return UserList(emptyList(),false, -1)
        val response = userDataSource.getUserListByName(name, cursorId)
        val data = response.getOrThrow()
        return data.toUserList()
    }
}
