package com.bff.wespot.auth.screen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.bff.wespot.auth.R
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.button.WSOutlineButton
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun CompleteScreen() {
    var dialog by remember {
        mutableStateOf(false)
    }

    val activity = (LocalContext.current as? Activity)

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Column {
            WSButton(onClick = { }, text = stringResource(id = R.string.invite_friend_and_start)) {
                it.invoke()
            }

            WSOutlineButton(onClick = { }, text = stringResource(id = R.string.start)) {
                it.invoke()
            }
        }
    }

    BackHandler {
        dialog = true
    }

    if (dialog) {
        WSDialog(
            title = stringResource(id = R.string.finish_check),
            subTitle = stringResource(id = R.string.finish_check_detail),
            onDismissRequest = {
                dialog = false
            },
            okButtonClick = {
                activity?.finish()
            },
            cancelButtonClick = {
                dialog = false
            },
            okButtonText = stringResource(id = R.string.ok),
            cancelButtonText = stringResource(id = R.string.cancel),
        )
    }
}
