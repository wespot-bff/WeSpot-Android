package com.bff.wespot.message.screen.setting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.message.R
import com.bff.wespot.message.state.setting.MessageNotificationSettingAction
import com.bff.wespot.message.viewmodel.MessageNotificationSettingViewModel
import com.bff.wespot.ui.component.LoadingAnimation
import com.bff.wespot.ui.component.SettingSwitchItem
import com.ramcosta.composedestinations.annotation.Destination
import org.orbitmvi.orbit.compose.collectAsState

interface MessageNotificationSettingNavigator {
    fun navigateUp()
}

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun MessageNotificationSettingScreen(
    viewModel: MessageNotificationSettingViewModel = hiltViewModel(),
    navigator: MessageNotificationSettingNavigator,
) {
    val action = viewModel::onAction
    val state by viewModel.collectAsState()

    if (state.isLoading) {
        LoadingAnimation()
        return
    }

    Scaffold(
        topBar = {
            WSTopBar(
                title = "",
                canNavigateBack = true,
                navigateUp = { navigator.navigateUp() },
            )
        },
    ) {
        Column(
            modifier = Modifier.padding(it),
        ) {
            Text(
                modifier = Modifier.padding(bottom = 16.dp, start = 30.dp, end = 30.dp),
                text = stringResource(R.string.message_usage_title),
                style = StaticTypeScale.Default.header1,
                color = WeSpotThemeManager.colors.txtTitleColor,
            )

            Box(
                modifier = Modifier.padding(top = 6.dp, start = 30.dp, end = 18.dp),
            ) {
                SettingSwitchItem(
                    title = stringResource(R.string.message_notification_setting_title),
                    subTitle = stringResource(R.string.message_notification_setting_subtitle),
                    switchValue = state.notificationSetting.isEnableMessageNotification,
                    onSwitched = {
                        action(MessageNotificationSettingAction.OnNotificationSettingSwitched)
                    },
                )
            }
        }
    }

    LifecycleStartEffect(Unit) {
        onStopOrDispose {
            action(MessageNotificationSettingAction.OnLifecycleStop)
        }
    }
}
