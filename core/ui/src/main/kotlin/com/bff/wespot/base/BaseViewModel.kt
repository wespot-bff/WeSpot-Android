package com.bff.wespot.base

import androidx.lifecycle.ViewModel
import com.bff.wespot.network.NetworkStateChecker
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

open class BaseViewModel : ViewModel() {
    @Inject
    lateinit var networkStateChecker: NetworkStateChecker

    @Inject
    lateinit var coroutineDispatcher: CoroutineDispatcher

    val networkState
        get() = networkStateChecker.networkState
}
