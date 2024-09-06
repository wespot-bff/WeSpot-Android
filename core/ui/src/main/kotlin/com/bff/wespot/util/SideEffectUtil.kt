package com.bff.wespot.util

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge
import org.orbitmvi.orbit.ContainerHost

@SuppressLint("ComposableNaming")
@Composable
public fun <STATE : Any, SIDE_EFFECT : Any> ContainerHost<STATE, SIDE_EFFECT>.collectSideEffect(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    baseSideEffectFlow: Flow<SIDE_EFFECT>,
    sideEffect: (suspend (sideEffect: SIDE_EFFECT) -> Unit),
) {
    val sideEffectFlow = container.refCountSideEffectFlow
    val lifecycleOwner = LocalLifecycleOwner.current

    val callback by rememberUpdatedState(newValue = sideEffect)

    LaunchedEffect(sideEffectFlow, baseSideEffectFlow, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(lifecycleState) {
            merge(sideEffectFlow, baseSideEffectFlow).collect { callback(it) }
        }
    }
}
