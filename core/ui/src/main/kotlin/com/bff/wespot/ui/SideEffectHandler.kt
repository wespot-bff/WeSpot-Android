package com.bff.wespot.ui

import androidx.compose.runtime.Composable
import com.bff.wespot.designsystem.component.indicator.WSToastType
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.designsystem.component.modal.WSDialogType
import com.bff.wespot.model.SideEffect
import com.bff.wespot.model.SideEffectState

@Composable
fun SideEffectHandler(
    state: SideEffectState,
    onDismiss: () -> Unit,
    onNavigate: () -> Unit,
) {
    if (state.show) {
        when (state.sideEffect) {
            is SideEffect.ShowToast -> TopToast(
                message = state.sideEffect.message,
                toastType = WSToastType.Error,
                showToast = true,
                closeToast = onDismiss,
            )

            is SideEffect.ShowDialog -> WSDialog(
                dialogType = WSDialogType.OneButton,
                title = state.sideEffect.message,
                okButtonClick = onDismiss,
                onDismissRequest = { },
            )

            SideEffect.Redirect -> onNavigate()
        }
    }
}
