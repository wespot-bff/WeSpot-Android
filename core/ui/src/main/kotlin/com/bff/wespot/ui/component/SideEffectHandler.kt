package com.bff.wespot.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.bff.wespot.designsystem.component.indicator.WSToastType
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.designsystem.component.modal.WSDialogType
import com.bff.wespot.ui.model.SideEffect

@Composable
fun SideEffectHandler(
    effect: SideEffect,
    onNavigate: () -> Unit = { },
    onDismiss: () -> Unit = { },
) {
    var isImpression by remember { mutableStateOf(false) }

    LaunchedEffect(effect) {
        // 현재 노출 중인 컴포넌트가 없는지, SideEffect가 소비된 상태인지 확인
        if (isImpression.not() && effect != SideEffect.Consumed) {
            isImpression = true
        }
    }

    if (isImpression) {
        when (effect) {
            SideEffect.Consumed -> { }

            SideEffect.Redirect -> onNavigate()

            is SideEffect.ShowToast -> {
                TopToast(
                    message = effect.message,
                    toastType = WSToastType.Error,
                    showToast = true,
                    closeToast = {
                        isImpression = false
                        onDismiss()
                    },
                )
            }

            is SideEffect.ShowDialog -> WSDialog(
                dialogType = WSDialogType.OneButton,
                title = effect.message,
                okButtonClick = {
                    isImpression = false
                    onDismiss()
                },
                onDismissRequest = { },
            )
        }
    }
}
