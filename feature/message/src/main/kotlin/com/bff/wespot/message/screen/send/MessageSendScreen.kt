package com.bff.wespot.message.screen.send

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bff.wespot.designsystem.component.button.HeightRange
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.button.WSButtonType
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.indicator.WSToastType
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.designsystem.component.toggle.WSSwitch
import com.bff.wespot.designsystem.theme.Gray400
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.message.R
import com.bff.wespot.message.component.ProfileSelectBottomSheet
import com.bff.wespot.message.component.SendExitDialog
import com.bff.wespot.message.state.send.SendAction
import com.bff.wespot.message.state.send.SendSideEffect
import com.bff.wespot.message.viewmodel.SendViewModel
import com.bff.wespot.ui.component.BottomButtonLayout
import com.bff.wespot.ui.component.LetterCountIndicator
import com.bff.wespot.ui.component.LoadingAnimation
import com.bff.wespot.ui.component.NetworkDialog
import com.bff.wespot.ui.component.ProfileCircleImage
import com.bff.wespot.ui.model.ToastState
import com.bff.wespot.ui.util.handleSideEffect
import com.ramcosta.composedestinations.annotation.Destination
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

interface MessageSendNavigator {
    fun navigateUp()
    fun navigateReceiverSelectionScreen(args: ReceiverSelectionScreenArgs)
    fun navigateMessageWriteScreen(args: MessageWriteScreenArgs)
    fun popUpToMessageScreen()
}

@Destination
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageSendScreen(
    navigator: MessageSendNavigator,
    showToast: (ToastState) -> Unit,
    viewModel: SendViewModel,
) {
    var exitDialog by remember { mutableStateOf(false) }
    var reserveDialog by remember { mutableStateOf(false) }
    var timeoutDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val state by viewModel.collectAsState()
    val action = viewModel::onAction

    val networkState by viewModel.networkState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val pickImage =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
            it?.let {
                action(SendAction.OnProfileImagePicked(it.toString()))
            }
        }

    handleSideEffect(viewModel.sideEffect)

    viewModel.collectSideEffect {
        when (it) {
            SendSideEffect.CloseReserveDialog -> {
                reserveDialog = false
            }

            SendSideEffect.ShowTimeoutDialog -> {
                timeoutDialog = true
            }

            SendSideEffect.NavigateToMessage -> {
                navigator.popUpToMessageScreen()
            }

            is SendSideEffect.ShowToast -> {
                showToast(
                    ToastState(
                        message = it.message,
                        show = true,
                        type = WSToastType.Success,
                    ),
                )
            }

            SendSideEffect.DismissExitDialog -> {
                exitDialog = false
            }

            SendSideEffect.NavigateUp -> navigator.navigateUp()

            SendSideEffect.OpenPicker -> {
                pickImage.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.SingleMimeType(
                            "image/*",
                        ),
                    ),
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
                    action(SendAction.OnTopBarNavigateButtonClicked)
                },
                action = {
                    Text(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable {
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
        BottomButtonLayout(
            modifier = Modifier.padding(it),
            button = {
                WSButton(
                    onClick = {
                        reserveDialog = true
                    },
                    text = stringResource(R.string.message_send),
                    content = { it() },
                )
            },
        ) {
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                MessageProfileItem(
                    title = stringResource(R.string.receiver),
                    buttonText = state.selectedUser.toMessageReceiverInfo(),
                    imageUrl = state.selectedUser.profileCharacter.iconUrl,
                    contentDescription = stringResource(R.string.receiver_profile_image),
                ) {
                    navigator.navigateReceiverSelectionScreen(
                        args = ReceiverSelectionScreenArgs(isEditing = true),
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                MessageContentItem(
                    title = stringResource(R.string.message_sent_content),
                    buttonText = state.messageInput,
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

                MessageProfileItem(
                    title = stringResource(R.string.sender),
                    buttonText = if (state.isAnonymous) {
                        state.selectedAnonymousProfile.name
                    } else {
                        state.profile.toMessageReceiverInfo()
                    },
                    imageUrl = if (state.isAnonymous) {
                        state.selectedAnonymousProfile.image
                    } else {
                        state.profile.profileCharacter.iconUrl
                    },
                    contentDescription = stringResource(R.string.sender_profile_image),
                    onClicked = { },
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
                            text = stringResource(R.string.anonymous_nickname_title),
                            style = StaticTypeScale.Default.body1,
                            color = WeSpotThemeManager.colors.txtTitleColor,
                        )

                        Text(
                            text = stringResource(R.string.anonymous_nickname_subtitle),
                            style = StaticTypeScale.Default.body8,
                            color = Gray400,
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    WSSwitch(checked = state.isAnonymous) {
                        action(SendAction.OnAnonymousToggled)
                    }
                }
            }
        }

        if (exitDialog) {
            SendExitDialog(
                okButtonClick = {
                    action(SendAction.OnExitDialogExitButtonClicked)
                },
                cancelButtonClick = {
                    action(SendAction.OnExitDialogCancelButtonClicked)
                },
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
                onDismissRequest = { },
            )
        }

        if (timeoutDialog) {
            WSDialog(
                title = stringResource(R.string.timeout_dialog_title),
                subTitle = state.messageSendFailedDialogContent,
                okButtonText = stringResource(R.string.positive_answer),
                cancelButtonText = stringResource(R.string.close),
                okButtonClick = navigator::popUpToMessageScreen,
                cancelButtonClick = { timeoutDialog = false },
                onDismissRequest = { },
            )
        }

        if (state.showProfileSelectBottomSheet) {
            ProfileSelectBottomSheet(
                anonymousProfileList = state.anonymousProfileList,
                closeSheet = {
                    action(SendAction.OnProfileBottomSheetClosed)
                },
                onAnonymousProfileAddButtonClicked = {
                    action(SendAction.OnProfileAddButtonClicked)
                },
                onAnonymousProfileSelected = {
                    action(SendAction.OnProfileSelected(it))
                },
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
    }

    NetworkDialog(context = context, networkState = networkState)

    LaunchedEffect(Unit) {
        action(SendAction.OnMessageSendScreenEntered)
    }
}

@Composable
private fun MessageProfileItem(
    title: String,
    buttonText: String,
    imageUrl: String,
    contentDescription: String,
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
            paddingValues = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp),
            buttonType = WSButtonType.Tertiary,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 18.dp, end = 20.dp, top = 18.dp, bottom = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ProfileCircleImage(
                    size = 24.dp,
                    imageUrl = imageUrl,
                    contentDescription = contentDescription,
                )

                Text(
                    text = buttonText,
                    style = StaticTypeScale.Default.body4,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                )

                Icon(
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription = stringResource(R.string.edit_icon),
                )
            }
        }
    }
}

@Composable
private fun MessageContentItem(
    title: String,
    buttonText: String,
    onClicked: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column {
        Text(
            text = title,
            style = StaticTypeScale.Default.body4,
            modifier = Modifier.padding(horizontal = 30.dp),
        )

        WSButton(
            onClick = onClicked,
            heightRange = HeightRange(170.dp, 228.dp),
            paddingValues = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp),
            buttonType = WSButtonType.Tertiary,
        ) {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
                    .verticalScroll(scrollState),
                text = buttonText,
                style = StaticTypeScale.Default.body4,
            )
        }
    }
}
