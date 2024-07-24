package com.bff.wespot.message.screen.send

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.input.WsTextField
import com.bff.wespot.designsystem.component.input.WsTextFieldType
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.message.R
import com.bff.wespot.message.state.send.SendAction
import com.bff.wespot.message.viewmodel.SendViewModel
import com.bff.wespot.ui.LetterCountIndicator
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.compose.collectAsState

interface MessageWriteNavigator {
    fun navigateUp()
    fun navigateMessageScreen()
    fun navigateMessageEditScreen()
}

data class MessageWriteScreenArgs(
    val isEditing: Boolean,
)

@Destination(navArgsDelegate = MessageWriteScreenArgs::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageWriteScreen(
    navigator: MessageWriteNavigator,
    navArgs: MessageWriteScreenArgs,
    viewModel: SendViewModel,
) {
    var dialogState by remember { mutableStateOf(false) }
    val keyboard = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    val state by viewModel.collectAsState()
    val action = viewModel::onAction

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
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 20.dp),
        ) {
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
                onValueChange = {
                    action(SendAction.OnMessageChanged(it))
                },
                placeholder = stringResource(R.string.message_write_text_holder),
                isError = false,
                focusRequester = focusRequester,
                textFieldType = WsTextFieldType.Message,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (state.hasProfanity) {
                    Text(
                        modifier = Modifier.padding(top = 4.dp, start = 10.dp, end = 10.dp),
                        text = stringResource(R.string.has_profanity),
                        style = StaticTypeScale.Default.body7,
                        color = WeSpotThemeManager.colors.dangerColor,
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                LetterCountIndicator(currentCount = state.messageInput.length, maxCount = 200)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        WSButton(
            onClick = {
                navigator.navigateMessageEditScreen()
            },
            enabled = state.messageInput.length in 0..200 && state.hasProfanity.not(),
            text = stringResource(
                if (navArgs.isEditing) R.string.edit_done else R.string.write_done,
            ),
        ) {
            it()
        }
    }

    if (dialogState) {
        WSDialog(
            title = stringResource(R.string.send_exit_dialog_title),
            subTitle = stringResource(R.string.send_exit_dialog_subtitle),
            okButtonText = stringResource(R.string.send_exit_dialog_ok_button),
            cancelButtonText = stringResource(id = R.string.close),
            okButtonClick = {
                action(SendAction.NavigateToMessageHome)
                navigator.navigateMessageScreen()
            },
            cancelButtonClick = { dialogState = false },
            onDismissRequest = { dialogState = false },
        )
    }

    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
        delay(10)
        keyboard?.show()
    }

    LaunchedEffect(Unit) {
        action(SendAction.OnWriteScreenEntered)
    }
}
