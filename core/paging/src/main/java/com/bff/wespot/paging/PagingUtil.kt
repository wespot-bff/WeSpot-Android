package com.bff.wespot.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.bff.wespot.model.common.Cursor

fun <V : Any> createPager(
    cursorId: Int = -1,
    enablePlaceholders: Boolean = true,
    block: suspend (Int) -> Pair<List<V>, Cursor>,
): Pager<Int, V> {
    return Pager(
        config = PagingConfig(enablePlaceholders = enablePlaceholders, pageSize = 10),
        pagingSourceFactory = { BasePagingSource(cursorId, block) }
    )
}