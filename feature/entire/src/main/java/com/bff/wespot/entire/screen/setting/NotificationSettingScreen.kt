package com.bff.wespot.entire.screen.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.designsystem.component.modal.WSDialogType
import com.bff.wespot.designsystem.component.toggle.WSSwitch
import com.bff.wespot.designsystem.theme.Gray400
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.entire.R
import com.bff.wespot.entire.screen.state.notification.NotificationSettingAction
import com.bff.wespot.entire.screen.state.notification.NotificationSettingSideEffect
import com.bff.wespot.entire.viewmodel.NotificationSettingViewModel
import com.bff.wespot.ui.LoadingAnimation
import com.bff.wespot.util.OnLifecycleEvent
import com.bff.wespot.util.handleSideEffect
import com.ramcosta.composedestinations.annotation.Destination
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface NotificationSettingNavigator {
    fun navigateUp()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun NotificationSettingScreen(
    navigator: NotificationSettingNavigator,
    viewModel: NotificationSettingViewModel = hiltViewModel(),
) {
    var showMarketingDialog by remember { mutableStateOf(false) }
    var showMarketingResultDialog by remember { mutableStateOf(false) }

    val action = viewModel::onAction
    val state by viewModel.collectAsState()

    if (state.isLoading) {
        LoadingAnimation()
        return
    }

    handleSideEffect(viewModel.sideEffect)

    viewModel.collectSideEffect {
        when (it) {
            NotificationSettingSideEffect.ShowMarketingResultDialog -> {
                showMarketingResultDialog = true
            }

            NotificationSettingSideEffect.ShowMarketingDialog -> {
                showMarketingDialog = true
            }
        }
    }

    Scaffold(
        topBar = {
            WSTopBar(
                title = stringResource(id = R.string.notification_setting),
                canNavigateBack = true,
                navigateUp = { navigator.navigateUp() },
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(top = 16.dp, start = 24.dp, end = 24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            NotificationSettingItem(
                title = stringResource(R.string.vote),
                subTitle = stringResource(R.string.vote_notification_title),
                switchValue = state.isEnableVoteNotification,
                onSwitched = {
                    action(NotificationSettingAction.OnVoteNotificationSwitched)
                },
            )

            NotificationSettingItem(
                title = stringResource(R.string.message),
                subTitle = stringResource(R.string.message_notification_title),
                switchValue = state.isEnableMessageNotification,
                onSwitched = {
                    action(NotificationSettingAction.OnMessageNotificationSwitched)
                },
            )

            NotificationSettingItem(
                title = stringResource(R.string.event_benefit),
                subTitle = stringResource(R.string.event_benefit_notification_title),
                switchValue = state.isEnableMarketingNotification,
                onSwitched = {
                    action(NotificationSettingAction.OnMarketingNotificationSwitched)
                },
            )
        }
    }

    if (showMarketingDialog) {
        WSDialog(
            title = "광고성 정보 수신 동의",
            subTitle = "푸시 알림을 통해 이벤트, 혜택 등을 알려드릴게요.\n앱 푸시 수신에 동의하시겠습니까?",
            okButtonText = "동의",
            cancelButtonText = "미동의",
            okButtonClick = {
                showMarketingDialog = false
                action(NotificationSettingAction.SetMarketingNotificationEnable)
                showMarketingResultDialog = true
            },
            cancelButtonClick = {
                showMarketingDialog = false
            },
            onDismissRequest = { },
        )
    }

    if (showMarketingResultDialog) {
        WSDialog(
            title = "광고성 정보 수신 동의 처리 결과",
            subTitle = stringResource(
                R.string.marketing_dialog_result_content,
                getCurrentDateTime(),
                if (state.isEnableMarketingNotification) {
                    stringResource(R.string.marketing_notification_agree)
                } else {
                    stringResource(R.string.marketing_notification_disagree)
                },
            ),
            okButtonText = stringResource(R.string.close),
            okButtonClick = { showMarketingResultDialog = false },
            onDismissRequest = { },
            dialogType = WSDialogType.OneButton,
        )
    }

    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_PAUSE -> {
                action(NotificationSettingAction.OnNotificationSettingScreenExited)
            }
            else -> { }
        }
    }

    LaunchedEffect(Unit) {
        action(NotificationSettingAction.OnNotificationSettingScreenEntered)
    }
}

@Composable
fun NotificationSettingItem(
    title: String,
    subTitle: String,
    switchValue: Boolean,
    onSwitched: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = title,
                style = StaticTypeScale.Default.body2,
                color = WeSpotThemeManager.colors.txtTitleColor,
            )

            Text(
                text = subTitle,
                style = StaticTypeScale.Default.body8,
                color = Gray400,
            )
        }

        WSSwitch(
            checked = switchValue,
            onCheckedChange = onSwitched,
        )
    }
}

private fun getCurrentDateTime(): String {
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 HH시 mm분")
    return currentDateTime.format(formatter)
}
