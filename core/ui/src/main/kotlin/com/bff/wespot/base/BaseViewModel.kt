package com.bff.wespot.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.model.BaseSideEffect
import com.bff.wespot.network.NetworkStateChecker
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

open class BaseViewModel : ViewModel() {
    @Inject
    lateinit var networkStateChecker: NetworkStateChecker

    @Inject
    lateinit var coroutineDispatcher: CoroutineDispatcher

    val networkState
        get() = networkStateChecker.networkState

    private val _sideEffect = Channel<BaseSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    protected fun postSideEffect(event: BaseSideEffect) {
        viewModelScope.launch {
            _sideEffect.send(event)
        }
    }
}
