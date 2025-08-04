package com.bff.wespot.model.community

import com.bff.wespot.model.common.Paging

data class CommunityContentPaging(
    override val data: List<BaseCommunityContent>,
    override val lastCursorId: Int,
    override val hasNext: Boolean,
) : Paging<BaseCommunityContent>
