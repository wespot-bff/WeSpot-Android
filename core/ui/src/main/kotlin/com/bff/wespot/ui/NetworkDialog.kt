package com.bff.wespot.ui

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bff.wespot.designsystem.component.modal.WSDialog


@Composable
fun NetworkDialog(
    context: Context,
    closeDialog: () -> Unit,
) {
    WSDialog(
        title = stringResource(R.string.no_internet),
        subTitle = stringResource(R.string.enable_wifi),
        okButtonText = stringResource(R.string.ok),
        cancelButtonText = stringResource(R.string.setting),
        okButtonClick = closeDialog,
        cancelButtonClick = {
            Intent(Settings.ACTION_WIRELESS_SETTINGS).also {
                context.startActivity(it)
            }
        },
        onDismissRequest = closeDialog,
    )
}