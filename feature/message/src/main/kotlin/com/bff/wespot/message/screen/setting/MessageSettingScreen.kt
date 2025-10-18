package com.bff.wespot.message.screen.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bff.wespot.analytics.TrackScreenViewEvent
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.message.R
import com.bff.wespot.ui.component.SettingListItem
import com.ramcosta.composedestinations.annotation.Destination

interface MessageSettingNavigator {
    fun navigateUp()
    fun navigateToBlockedMessageScreen()
    fun navigateToMessageNotificationSettingScreen()
    fun navigateToMessageUsageSettingScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun MessageSettingScreen(
    navigator: MessageSettingNavigator,
) {
    Scaffold(
        topBar = {
            WSTopBar(
                title = stringResource(R.string.message_setting),
                canNavigateBack = true,
                navigateUp = { navigator.navigateUp() },
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(top = 4.dp, start = 24.dp, end = 24.dp),
        ) {
            SettingListItem(stringResource(R.string.blocked_message_title)) {
                navigator.navigateToBlockedMessageScreen()
            }

            SettingListItem(text = stringResource(R.string.message_usage_setting)) {
                navigator.navigateToMessageUsageSettingScreen()
            }

            SettingListItem(stringResource(R.string.message_notification_setting)) {
                navigator.navigateToMessageNotificationSettingScreen()
            }
        }
    }

    TrackScreenViewEvent("view_message_setting")
}
