package com.bff.wespot.ui.base

import androidx.lifecycle.ViewModel
import com.bff.wespot.ui.model.SideEffect
import com.bff.wespot.ui.network.NetworkStateChecker
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

open class BaseViewModel : ViewModel() {
    @Inject
    lateinit var networkStateChecker: NetworkStateChecker

    @Inject
    lateinit var coroutineDispatcher: CoroutineDispatcher

    val networkState
        get() = networkStateChecker.networkState

    private val _sideEffect = Channel<SideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    protected suspend fun postSideEffect(sideEffect: SideEffect) = _sideEffect.send(sideEffect)
}
