package com.bff.wespot.message.screen.send

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.input.WsTextField
import com.bff.wespot.designsystem.component.input.WsTextFieldType
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.designsystem.theme.Gray300
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.message.R
import com.bff.wespot.message.state.send.SendAction
import com.bff.wespot.message.viewmodel.SendViewModel
import com.bff.wespot.ui.UserListItem
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.compose.collectAsState

interface ReceiverSelectionNavigator {
    fun navigateUp()
    fun navigateMessageWriteScreen(args: MessageWriteScreenArgs)
    fun navigateMessageScreen()
}

data class ReceiverSelectionScreenArgs(
    val isEditing: Boolean,
)

@Destination(navArgsDelegate = ReceiverSelectionScreenArgs::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiverSelectionScreen(
    navigator: ReceiverSelectionNavigator,
    navArgs: ReceiverSelectionScreenArgs,
    viewModel: SendViewModel,
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    var dialogState by remember { mutableStateOf(false) }

    val state by viewModel.collectAsState()
    val action = viewModel::onAction

    Scaffold(
        topBar = {
            WSTopBar(
                title = "",
                action = {
                    Text(
                        modifier = Modifier.clickable {
                            dialogState = true
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
                .padding(it)
                .padding(horizontal = 20.dp),
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 4.dp),
                text = stringResource(R.string.receiver_screen_title),
                style = StaticTypeScale.Default.header1,
                color = WeSpotThemeManager.colors.txtTitleColor,
            )

            Spacer(modifier = Modifier.height(16.dp))

            WsTextField(
                value = state.nameInput,
                onValueChange = {
                    action(SendAction.OnSearchContentChanged(it))
                },
                placeholder = stringResource(R.string.receiver_search_text_field_placeholder),
                textFieldType = WsTextFieldType.Search,
                focusRequester = focusRequester,
                singleLine = true,
            )

            if (state.userList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        modifier = Modifier
                            .drawBehind {
                                drawLine(
                                    strokeWidth = 1f * density,
                                    color = Gray300,
                                    start = Offset(0f, size.height),
                                    end = Offset(size.width, size.height),
                                )
                            }
                            .clickable {
                                action(SendAction.OnInviteFriendTextClicked)
                            },
                        text = stringResource(R.string.invite_friend_text),
                        style = StaticTypeScale.Default.body5,
                        color = WeSpotThemeManager.colors.txtSubColor,
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.padding(top = 16.dp),
            ) {
                items(state.userList, key = { user -> user.id }) { item ->
                    UserListItem(
                        name = item.name,
                        schoolInfo = item.toSchoolInfo(),
                        selected = state.selectedUser.id == item.id,
                        backgroundColor = item.profileCharacter.backgroundColor,
                        iconUrl = item.profileCharacter.iconUrl,
                        onClick = {
                            action(SendAction.OnUserSelected(item))
                        },
                    )
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            WeSpotThemeManager.colors.backgroundColor,
                        ),
                    ),
                ),
            contentAlignment = Alignment.BottomCenter,
        ) {
            WSButton(
                onClick = {
                    if (navArgs.isEditing) {
                        navigator.navigateUp()
                        return@WSButton
                    }
                    navigator.navigateMessageWriteScreen(
                        args = MessageWriteScreenArgs(isEditing = false),
                    )
                },
                enabled = state.selectedUser.name.isNotBlank(),
                text = if (navArgs.isEditing) stringResource(R.string.edit_done) else stringResource(R.string.next),
            ) {
                it()
            }
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
        action(SendAction.OnReceiverScreenEntered)
    }
}