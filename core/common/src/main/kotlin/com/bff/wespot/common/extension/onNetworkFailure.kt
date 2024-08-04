package com.bff.wespot.common.extension

import com.bff.wespot.model.exception.NetworkException

inline fun <T> Result<T>.onNetworkFailure(action: (NetworkException) -> Unit): Result<T> {
    return this.onFailure { exception ->
        val networkException = exception as? NetworkException
        if (networkException != null) {
            action(networkException)
        }
    }
}