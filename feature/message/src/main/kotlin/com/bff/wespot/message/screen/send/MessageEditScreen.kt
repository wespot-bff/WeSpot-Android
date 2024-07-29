package com.bff.wespot.message.screen.send

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.bff.wespot.designsystem.component.button.HeightRange
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.button.WSButtonType
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.indicator.WSToast
import com.bff.wespot.designsystem.component.indicator.WSToastType
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.designsystem.component.toggle.WSSwitch
import com.bff.wespot.designsystem.theme.Gray400
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.message.R
import com.bff.wespot.message.screen.MessageScreenArgs
import com.bff.wespot.message.screen.ReservedMessageScreenArgs
import com.bff.wespot.message.state.send.SendAction
import com.bff.wespot.message.state.send.SendSideEffect
import com.bff.wespot.message.viewmodel.SendViewModel
import com.bff.wespot.ui.LetterCountIndicator
import com.ramcosta.composedestinations.annotation.Destination
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

interface MessageEditNavigator {
    fun navigateUp()
    fun navigateReceiverSelectionScreen(args: ReceiverSelectionScreenArgs)
    fun navigateMessageWriteScreen(args: MessageWriteScreenArgs)
    fun navigateMessageScreen(args: MessageScreenArgs)
    fun navigateToReservedMessageScreen(args: ReservedMessageScreenArgs)
}

data class EditMessageScreenArgs(
    val isReservedMessage: Boolean = false,
    val messageId: Int = -1,
)

@Destination(navArgsDelegate = EditMessageScreenArgs::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageEditScreen(
    navigator: MessageEditNavigator,
    navArgs: EditMessageScreenArgs,
    viewModel: SendViewModel,
) {
    var exitDialog by remember { mutableStateOf(false) }
    var reserveDialog by remember { mutableStateOf(false) }
    var timeoutDialog by remember { mutableStateOf(false) }
    var toast by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val state by viewModel.collectAsState()
    val action = viewModel::onAction

    viewModel.collectSideEffect {
        when (it) {
            is SendSideEffect.ShowTimeoutDialog -> {
                reserveDialog = false
                timeoutDialog = true
            }

            is SendSideEffect.NavigateToMessage -> {
                navigator.navigateMessageScreen(args = MessageScreenArgs(true))
            }

            is SendSideEffect.NavigateToReservedMessage -> {
                navigator.navigateToReservedMessageScreen(
                    args = ReservedMessageScreenArgs(true),
                )
            }
        }
    }

    Scaffold(
        topBar = {
            WSTopBar(
                title = "",
                canNavigateBack = true,
                navigateUp = {
                    navigator.navigateUp()
                },
                action = {
                    Text(
                        modifier = Modifier.clickable {
                            exitDialog = true
                        },
                        text = stringResource(R.string.close),
                        style = StaticTypeScale.Default.body4,
                        color = WeSpotThemeManager.colors.abledTxtColor,
                    )
                },
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(scrollState),
        ) {
            EditField(
                title = stringResource(R.string.receiver),
                value = state.selectedUser.toDescription(),
            ) {
                navigator.navigateReceiverSelectionScreen(
                    args = ReceiverSelectionScreenArgs(isEditing = true),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            EditField(
                title = stringResource(R.string.message_sent_content),
                value = state.messageInput,
                isMessageContent = true,
            ) {
                navigator.navigateMessageWriteScreen(
                    args = MessageWriteScreenArgs(isEditing = true),
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                LetterCountIndicator(currentCount = state.messageInput.length, maxCount = 200)
            }

            EditField(
                title = stringResource(R.string.sender),
                value = if (state.isRandomName) state.randomName else state.sender,
                onClicked = { toast = true },
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, start = 30.dp, end = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        text = stringResource(R.string.random_nickname_title),
                        style = StaticTypeScale.Default.body1,
                        color = WeSpotThemeManager.colors.txtTitleColor,
                    )

                    Text(
                        text = stringResource(R.string.random_nickname_subtitle),
                        style = StaticTypeScale.Default.body8,
                        color = Gray400,
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                WSSwitch(checked = state.isRandomName) {
                    action(SendAction.OnRandomNameToggled(it))
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            WSButton(
                onClick = {
                    if (state.isReservedMessage) {
                        action(SendAction.OnEditButtonClicked(navArgs.messageId))
                    } else {
                        reserveDialog = true
                    }
                },
                paddingValues = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    top = 32.dp,
                    bottom = 12.dp,
                ),
                text = stringResource(
                    if (state.isReservedMessage) R.string.edit_done else R.string.message_send,
                ),
            ) {
                it()
            }
        }

        if (exitDialog) {
            WSDialog(
                title = stringResource(
                    if (state.isReservedMessage) {
                        R.string.edit_exit_dialog_title
                    } else {
                        R.string.send_exit_dialog_title
                    },
                ),
                subTitle = stringResource(R.string.send_exit_dialog_subtitle),
                okButtonText = stringResource(R.string.send_exit_dialog_ok_button),
                cancelButtonText = stringResource(id = R.string.close),
                okButtonClick = {
                    navigator.navigateMessageScreen(args = MessageScreenArgs(false))
                },
                cancelButtonClick = { exitDialog = false },
                onDismissRequest = { exitDialog = false },
            )
        }

        if (reserveDialog) {
            WSDialog(
                title = stringResource(R.string.message_send_dialog_title),
                subTitle = stringResource(R.string.message_send_dialog_subtitle),
                okButtonText = stringResource(R.string.message_send_dialog_button_text),
                cancelButtonText = stringResource(R.string.cancel),
                okButtonClick = { action(SendAction.OnSendButtonClicked) },
                cancelButtonClick = { reserveDialog = false },
                onDismissRequest = { reserveDialog = false },
            )
        }

        if (timeoutDialog) {
            WSDialog(
                title = stringResource(R.string.timeout_dialog_title),
                subTitle = stringResource(R.string.timeout_dialog_subtitle),
                okButtonText = stringResource(R.string.positive_answer),
                cancelButtonText = stringResource(R.string.close),
                okButtonClick = {
                    navigator.navigateMessageScreen(args = MessageScreenArgs(false))
                },
                cancelButtonClick = { timeoutDialog = false },
                onDismissRequest = { timeoutDialog = false },
            )
        }
    }

    if (toast) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            WSToast(
                text = stringResource(R.string.toast_error_name_edit),
                toastType = WSToastType.Error,
                showToast = toast,
                closeToast = { toast = false },
            )
        }
    }

    LaunchedEffect(Unit) {
        action(SendAction.OnMessageEditScreenEntered(navArgs.isReservedMessage, navArgs.messageId))
    }
}

@Composable
private fun EditField(
    title: String,
    value: String,
    isMessageContent: Boolean = false,
    onClicked: () -> Unit,
) {
    Column {
        Text(
            text = title,
            style = StaticTypeScale.Default.body4,
            modifier = Modifier.padding(horizontal = 30.dp),
        )

        WSButton(
            onClick = onClicked,
            heightRange = if (isMessageContent) HeightRange(170.dp, 228.dp) else null,
            paddingValues = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp),
            buttonType = WSButtonType.Tertiary,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
            ) {
                Text(
                    text = value,
                    style = StaticTypeScale.Default.body4,
                )
            }
        }
    }
}
