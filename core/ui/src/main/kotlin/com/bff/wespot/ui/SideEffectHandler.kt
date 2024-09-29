package com.bff.wespot.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.bff.wespot.designsystem.component.indicator.WSToastType
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.designsystem.component.modal.WSDialogType
import com.bff.wespot.model.SideEffect

@Composable
fun SideEffectHandler(
    effect: SideEffect,
    onNavigate: () -> Unit = { },
) {
    var isSideEffectHandled by remember { mutableStateOf(true) }

    LaunchedEffect(effect) {
        isSideEffectHandled = effect == SideEffect.None
    }

    if (isSideEffectHandled.not()) {
        when (effect) {
            SideEffect.None -> { }

            SideEffect.Redirect -> onNavigate()

            is SideEffect.ShowToast -> {
                TopToast(
                    message = effect.message,
                    toastType = WSToastType.Error,
                    showToast = true,
                    closeToast = {
                        isSideEffectHandled = true
                    },
                )
            }

            is SideEffect.ShowDialog -> WSDialog(
                dialogType = WSDialogType.OneButton,
                title = effect.message,
                okButtonClick = {
                    isSideEffectHandled = true
                },
                onDismissRequest = { },
            )
        }
    }
}
