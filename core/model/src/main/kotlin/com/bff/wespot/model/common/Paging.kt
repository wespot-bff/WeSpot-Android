package com.bff.wespot.model.common

interface Paging<T> {
    val data: List<T>
    val lastCursorId: Int
    val hasNext: Boolean
}
