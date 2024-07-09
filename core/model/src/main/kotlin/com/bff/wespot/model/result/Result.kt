package com.bff.wespot.model.result

sealed interface Result<out D, out E : BaseError> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E : BaseError>(val error: E) : Result<Nothing, E>
}

inline fun <T, E : BaseError, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when (this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(map(data))
    }
}

inline fun <T, E : BaseError, R> Result<T, E>.map(
    mapData: (T) -> R,
    mapError: (E) -> NetworkError,
): Result<R, NetworkError> {
    return when (this) {
        is Result.Error -> Result.Error(mapError(error))
        is Result.Success -> Result.Success(mapData(data))
    }
}

inline fun <T, E : BaseError> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> this
        is Result.Success -> {
            action(data)
            this
        }
    }
}

inline fun <T, E : BaseError> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> {
            action(error)
            this
        }
        is Result.Success -> this
    }
}
