package com.bff.wespot.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.bff.wespot.base.BaseViewModel
import com.bff.wespot.model.SideEffect

@Composable
fun BaseViewModel.collectSideEffectAsState(
    lifecycleState: Lifecycle.State = androidx.lifecycle.Lifecycle.State.STARTED,
): State<SideEffect> {
    val sideEffectFlow = this.sideEffect
    val lifecycleOwner = LocalLifecycleOwner.current

    return produceState<SideEffect>(
        initialValue = SideEffect.None,
        key1 = sideEffectFlow,
        key2 = lifecycleOwner,
    ) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(lifecycleState) {
            sideEffectFlow.collect { sideEffect ->
                value = sideEffect
            }
        }
    }
}
