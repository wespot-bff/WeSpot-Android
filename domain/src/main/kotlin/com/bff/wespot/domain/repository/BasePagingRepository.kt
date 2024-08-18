package com.bff.wespot.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bff.wespot.domain.paging.BasePagingSource
import com.bff.wespot.model.common.Paging
import kotlinx.coroutines.flow.Flow

abstract class BasePagingRepository<T : Any, R : Paging<T>> {
    protected abstract fun pagingSource(parameter: Map<String, String>? = null): BasePagingSource<T, R>

    fun fetchResultStream(parameter: Map<String, String>? = null): Flow<PagingData<T>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { pagingSource(parameter) },
    ).flow
}
