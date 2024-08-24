package com.bff.wespot.domain.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bff.wespot.model.common.Paging
import java.io.IOException

abstract class BasePagingSource<T : Any, R : Paging<T>> : PagingSource<Int, T>() {
    protected abstract suspend fun fetchItems(cursorId: Int?): R

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key
        return try {
            val response = fetchItems(page)
            val lastCursorId = response.lastCursorId
            val hasNext = response.hasNext

            val nextKey = when {
                hasNext -> lastCursorId
                else -> null
            }

            LoadResult.Page(
                data = response.data,
                prevKey = null,
                nextKey = nextKey,
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}
