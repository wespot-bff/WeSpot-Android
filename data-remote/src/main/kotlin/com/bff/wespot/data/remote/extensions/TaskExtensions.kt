package com.bff.wespot.data.remote.extensions

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T> Task<T>.await(): T {
    return suspendCancellableCoroutine { cont ->
        addOnCompleteListener {
            val exception = it.exception
            if (exception != null) {
                cont.resumeWithException(exception)
                Timber.e(exception)
            } else {
                cont.resume(it.result, null)
                Timber.d(it.result.toString())
            }
        }
    }
}
