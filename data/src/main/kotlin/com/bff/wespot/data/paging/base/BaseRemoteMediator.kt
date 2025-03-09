package com.bff.wespot.data.paging.base

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.bff.wespot.model.common.Paging
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
abstract class BaseRemoteMediator<T : Any, R : Paging<T>> : RemoteMediator<Int, T>() {
    protected abstract suspend fun fetchItems(loadCursorId: Int?): R

    protected abstract suspend fun saveItems(loadType: LoadType, items: List<T>)

    protected abstract fun getNextCursorId(item: T): Int?

    override suspend fun load(loadType: LoadType, state: PagingState<Int, T>): MediatorResult {
        val loadCursorId = when (loadType) {
            LoadType.REFRESH -> null
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> state.lastItemOrNull()?.let { getNextCursorId(it) }
        }
        return try {
            val response = fetchItems(loadCursorId)
            saveItems(loadType, response.data)
            MediatorResult.Success(endOfPaginationReached = !response.hasNext)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
