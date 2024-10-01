package com.bff.wespot.util

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.bff.wespot.model.SideEffect
import kotlinx.coroutines.flow.Flow

@SuppressLint("ComposableNaming")
@Composable
fun Flow<SideEffect>.collectSideEffect(
    lifecycleState: Lifecycle.State = androidx.lifecycle.Lifecycle.State.STARTED,
    sideEffect: (suspend (sideEffect: SideEffect) -> Unit),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val callback by rememberUpdatedState(newValue = sideEffect)

    LaunchedEffect(this, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(lifecycleState) {
            this@collectSideEffect.collect {
                callback(it)
            }
        }
    }
}
