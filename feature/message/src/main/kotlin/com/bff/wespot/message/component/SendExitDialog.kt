package com.bff.wespot.message.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.message.R

@Composable
fun SendExitDialog(
    isReservedMessage: Boolean,
    okButtonClick: () -> Unit,
    cancelButtonClick: () -> Unit,
) {
    WSDialog(
        title = stringResource(
            if (isReservedMessage) {
                R.string.edit_exit_dialog_title
            } else {
                R.string.send_exit_dialog_title
            },
        ),
        subTitle = stringResource(R.string.send_exit_dialog_subtitle),
        okButtonText = stringResource(R.string.send_exit_dialog_ok_button),
        cancelButtonText = stringResource(id = R.string.close),
        okButtonClick = okButtonClick,
        cancelButtonClick = cancelButtonClick,
        onDismissRequest = { },
    )
}
