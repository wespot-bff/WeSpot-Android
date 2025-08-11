package com.bff.wespot.model.community

import com.bff.wespot.model.common.Paging
import com.bff.wespot.model.serverDriven.type.ImageType

data class CategoryDetailsPaging(
    override val data: List<BaseCommunityContent>,
    override val lastCursorId: Int,
    override val hasNext: Boolean,
    val background: ImageType,
    val thumbnail: ImageType,
) : Paging<BaseCommunityContent>
