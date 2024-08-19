package com.bff.wespot.model.user.response

import com.bff.wespot.model.common.Paging

data class UserList(
    override val data: List<User>,
    override val hasNext: Boolean,
    override val lastCursorId: Int,
) : Paging<User>
