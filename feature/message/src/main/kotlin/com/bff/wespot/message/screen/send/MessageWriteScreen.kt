package com.bff.wespot.message.screen.send

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.input.WsTextField
import com.bff.wespot.designsystem.component.input.WsTextFieldType
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.message.R
import com.bff.wespot.message.common.MESSAGE_MAX_LENGTH
import com.bff.wespot.message.component.ProfileSelectBottomSheet
import com.bff.wespot.message.component.SendExitDialog
import com.bff.wespot.message.state.send.SendAction
import com.bff.wespot.message.state.send.SendSideEffect
import com.bff.wespot.message.viewmodel.SendViewModel
import com.bff.wespot.ui.component.BottomButtonLayout
import com.bff.wespot.ui.component.LetterCountIndicator
import com.bff.wespot.ui.component.LoadingAnimation
import com.bff.wespot.ui.component.NetworkDialog
import com.bff.wespot.ui.model.ToastState
import com.bff.wespot.ui.util.handleSideEffect
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

interface MessageWriteNavigator {
    fun navigateUp()
    fun popUpToMessageScreen()
    fun navigateMessageSendScreen()
}

@Destination
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageWriteScreen(
    navigator: MessageWriteNavigator,
    viewModel: SendViewModel,
    showToast: (ToastState) -> Unit,
) {
    var dialogState by remember { mutableStateOf(false) }
    val keyboard = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    val state by viewModel.collectAsState()
    val action = viewModel::onAction
    val networkState by viewModel.networkState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    handleSideEffect(viewModel.sideEffect)

    viewModel.collectSideEffect {
        when (it) {
            SendSideEffect.DismissExitDialog -> {
                dialogState = false
            }

            SendSideEffect.NavigateToMessage -> {
                /** 키보드가 올라간 채로 화면 전환시, 화면이 일그러지는 것을 방지한다. */
                keyboard?.hide()
                navigator.popUpToMessageScreen()
            }

            SendSideEffect.NavigateUp -> navigator.navigateUp()

            SendSideEffect.NavigateToMessageSendScreen -> navigator.navigateMessageSendScreen()

            else -> { }
        }
    }

    Scaffold(
        topBar = {
            WSTopBar(
                title = "",
                canNavigateBack = true,
                navigateUp = {
                    action(SendAction.OnTopBarNavigateButtonClicked)
                },
                action = {
                    Text(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable {
                                dialogState = true
                            },
                        text = stringResource(id = R.string.close),
                        style = StaticTypeScale.Default.body4,
                        color = WeSpotThemeManager.colors.abledTxtColor,
                    )
                },
            )
        },
    ) {
        BottomButtonLayout(
            modifier = Modifier.padding(it),
            button = {
                WSButton(
                    onClick = {
                        action(SendAction.OnWriteDoneButtonClicked)
                    },
                    enabled = state.messageInput.length in 1..MESSAGE_MAX_LENGTH && state.hasProfanity.not(),
                    text = stringResource(R.string.write_done),
                    content = { it() },
                )
            },
        ) {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 3.dp),
                    text = stringResource(R.string.message_write_title),
                    style = StaticTypeScale.Default.header1,
                    color = WeSpotThemeManager.colors.txtTitleColor,
                )

                Spacer(modifier = Modifier.padding(top = 16.dp))

                WsTextField(
                    value = state.messageInput,
                    onValueChange = { text ->
                        action(SendAction.OnMessageChanged(text))
                    },
                    placeholder = stringResource(R.string.message_write_text_holder),
                    isError = false,
                    focusRequester = focusRequester,
                    textFieldType = WsTextFieldType.Message,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    val warningMessage = when {
                        state.messageInput.length > MESSAGE_MAX_LENGTH -> {
                            stringResource(R.string.message_length_limit)
                        }

                        state.hasProfanity -> {
                            stringResource(com.bff.wespot.designsystem.R.string.has_profanity)
                        }

                        else -> ""
                    }

                    Text(
                        modifier = Modifier.padding(top = 5.dp, start = 10.dp, end = 10.dp),
                        text = warningMessage,
                        style = StaticTypeScale.Default.body7,
                        color = WeSpotThemeManager.colors.dangerColor,
                    )

                    LetterCountIndicator(currentCount = state.messageInput.length, maxCount = 200)
                }
            }
        }
    }

    if (dialogState) {
        SendExitDialog(
            okButtonClick = {
                action(SendAction.OnExitDialogExitButtonClicked)
            },
            cancelButtonClick = {
                action(SendAction.OnExitDialogCancelButtonClicked)
            },
        )
    }

    if (state.showProfileSelectBottomSheet) {
        ProfileSelectBottomSheet(
            profileList = state.senderProfileList,
            closeSheet = {
                action(SendAction.OnProfileBottomSheetClosed)
            },
            onProfileAddButtonClicked = {
                action(SendAction.OnProfileAddButtonClicked)
            },
            onProfileSelected = {
                action(SendAction.OnProfileBottomSheetSelected(it))
            },
            showToast = showToast,
        )
    }

    if (state.showProfileCreatorModal) {
        ProfileCreatorModal(
            state = state,
            action = action,
        )
    }

    if (state.isLoading) {
        LoadingAnimation()
    }

    NetworkDialog(context = context, networkState = networkState)

    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
        delay(10)
        keyboard?.hide()
    }

    LaunchedEffect(Unit) {
        action(SendAction.OnWriteScreenEntered)
    }
}
