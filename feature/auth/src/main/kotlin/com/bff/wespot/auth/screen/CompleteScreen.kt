package com.bff.wespot.auth.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bff.wespot.auth.R
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.button.WSOutlineButton

@Composable
fun CompleteScreen() {
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
}
