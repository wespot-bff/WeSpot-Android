package com.bff.wespot.model.auth.response

import com.bff.wespot.model.common.Paging

data class SchoolList(
    override val data: List<School>,
    override val lastCursorId: Int,
    override val hasNext: Boolean,
) : Paging<School>
