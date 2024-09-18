package com.bff.wespot.util

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.bff.wespot.base.BaseViewModel
import com.bff.wespot.model.SideEffect

@SuppressLint("ComposableNaming")
@Composable
fun BaseViewModel.collectBaseSideEffect(
    lifecycleState: Lifecycle.State = androidx.lifecycle.Lifecycle.State.STARTED,
    sideEffect: (suspend (sideEffect: SideEffect) -> Unit),
) {
    val sideEffectFlow = this.sideEffect
    val lifecycleOwner = LocalLifecycleOwner.current

    val callback by rememberUpdatedState(newValue = sideEffect)

    LaunchedEffect(sideEffectFlow, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(lifecycleState) {
            sideEffectFlow.collect { callback(it) }
        }
    }
}
