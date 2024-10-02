package com.bff.wespot.ui.component

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.ui.R
import com.bff.wespot.ui.network.NetworkState

@Composable
fun NetworkDialog(
    context: Context,
    networkState: NetworkState,
) {
    var isDialogVisible by remember { mutableStateOf(false) }

    LaunchedEffect(networkState) {
        isDialogVisible = networkState is NetworkState.NotConnected
    }

    if (isDialogVisible) {
        WSDialog(
            title = stringResource(R.string.no_internet),
            subTitle = stringResource(R.string.enable_wifi),
            okButtonText = stringResource(R.string.ok),
            cancelButtonText = stringResource(R.string.setting),
            okButtonClick = {
                isDialogVisible = false
            },
            cancelButtonClick = {
                Intent(Settings.ACTION_WIRELESS_SETTINGS).also {
                    context.startActivity(it)
                }
            },
            onDismissRequest = {},
        )
    }
}
