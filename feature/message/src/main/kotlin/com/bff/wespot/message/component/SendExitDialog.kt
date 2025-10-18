package com.bff.wespot.message.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bff.wespot.analytics.LocalAnalyticsHelper
import com.bff.wespot.analytics.logClickAction
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.message.R

@Composable
fun SendExitDialog(
    okButtonClick: () -> Unit,
    cancelButtonClick: () -> Unit,
) {
    val analyticsHelper = LocalAnalyticsHelper.current

    WSDialog(
        title = stringResource(R.string.send_exit_dialog_title),
        subTitle = stringResource(R.string.send_exit_dialog_subtitle),
        okButtonText = stringResource(R.string.send_exit_dialog_ok_button),
        cancelButtonText = stringResource(id = R.string.close),
        okButtonClick = {
            analyticsHelper.logClickAction("click_exit_message_send")
            okButtonClick.invoke()
        },
        cancelButtonClick = cancelButtonClick,
        onDismissRequest = { },
    )
}
