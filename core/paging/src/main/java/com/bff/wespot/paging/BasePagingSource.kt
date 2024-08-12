package com.bff.wespot.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bff.wespot.model.common.Cursor

class BasePagingSource<V: Any>(
    private val cursorId: Int = -1,
    private val block: suspend (Int) -> Pair<List<V>, Cursor>
) : PagingSource<Int, V>() {
    override fun getRefreshKey(state: PagingState<Int, V>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, V> {
        val page = params.key ?: 1

        return try {
            val (response, cursor) = block(page)

            val nextKey = when {
                cursorId != -1 -> cursorId
                cursor.hasNext -> cursor.lastCursorId
                else -> null
            }

            LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = nextKey
            )
        } catch(e: Exception) {
            LoadResult.Error(e)
        }
    }
}