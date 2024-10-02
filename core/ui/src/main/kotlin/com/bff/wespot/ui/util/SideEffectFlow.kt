package com.bff.wespot.ui.util

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.bff.wespot.ui.component.SideEffectHandler
import com.bff.wespot.ui.model.SideEffect
import kotlinx.coroutines.flow.Flow

@SuppressLint("ComposableNaming")
@Composable
fun handleSideEffect(
    sideEffectFlow: Flow<SideEffect>,
    lifecycleState: Lifecycle.State = androidx.lifecycle.Lifecycle.State.STARTED,
    onNavigate: () -> Unit = { },
) {
    var sideEffectState by remember { mutableStateOf<SideEffect>(SideEffect.Consumed) }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(sideEffectFlow, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(lifecycleState) {
            sideEffectFlow.collect { sideEffect ->
                sideEffectState = sideEffect
            }
        }
    }

    SideEffectHandler(
        effect = sideEffectState,
        onDismiss = { sideEffectState = SideEffect.Consumed },
        onNavigate = onNavigate,
    )
}
