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
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.message.R
import com.bff.wespot.message.component.SendExitDialog
import com.bff.wespot.message.model.AnonymousProfile
import com.bff.wespot.message.state.send.send.SendAction
import com.bff.wespot.message.state.send.send.SendSideEffect
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
    fun popUpToMessageScreen()
    fun popUpToMessageWriteScreen()
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
    var showSendConfirmModal by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var showAnonymousProfileModal by remember { mutableStateOf(false) }

    val state by viewModel.collectAsState()
    val action: (SendAction) -> Unit = viewModel::onAction

    val networkState by viewModel.networkState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    handleSideEffect(viewModel.sideEffect)

    viewModel.collectSideEffect {
        if (it is SendSideEffect) {
            when (it) {
                SendSideEffect.NavigateToMessageWriteScreen -> {
                    navigator.popUpToMessageWriteScreen()
                }
                SendSideEffect.CloseSendConfirmModal -> {
                    showSendConfirmModal = false
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
                SendSideEffect.ShowAnonymousProfileModal -> {
                    showAnonymousProfileModal = true
                }
                SendSideEffect.DismissAnonymousProfileModal -> {
                    showAnonymousProfileModal = false
                }
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
                        showSendConfirmModal = true
                    },
                    text = stringResource(R.string.message_send),
                    content = { it() },
                )
            },
        ) {
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                MessageProfileItem(
                    title = stringResource(R.string.receiver),
                    buttonText = state.receiver.toMessageReceiverInfo(),
                    imageUrl = state.receiver.profileCharacter.iconUrl,
                    contentDescription = stringResource(R.string.receiver_profile_image),
                )

                Spacer(modifier = Modifier.height(16.dp))

                MessageContentItem(
                    title = stringResource(R.string.message_sent_content),
                    buttonText = state.messageInput,
                    onClick = {
                        action(SendAction.OnMessageContentClick)
                    },
                )

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
                    buttonText = state.senderProfile.name,
                    imageUrl = state.senderProfile.image,
                    contentDescription = stringResource(R.string.sender_profile_image),
                    onClicked = if (state.senderProfile.isAnonymous) {
                        { action(SendAction.OnSenderClicked) }
                    } else {
                        null
                    },
                )
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

        if (showSendConfirmModal) {
            WSDialog(
                title = stringResource(R.string.message_send_dialog_title),
                subTitle = stringResource(R.string.message_send_dialog_subtitle),
                okButtonText = stringResource(R.string.message_send_dialog_button_text),
                cancelButtonText = stringResource(R.string.cancel),
                okButtonClick = { action(SendAction.OnSendButtonClicked) },
                cancelButtonClick = { showSendConfirmModal = false },
                onDismissRequest = { },
            )
        }

        if (showAnonymousProfileModal) {
            AnonymousProfileModal(
                profile = AnonymousProfile(
                    name = state.senderProfile.name,
                    imageUrl = state.senderProfile.image,
                ),
                onProfileSelected = {
                    action(SendAction.OnAnonymousProfileSelected(it))
                },
                onDismiss = {
                    action(SendAction.OnAnonymousProfileModalDismiss)
                },
            )
        }

        if (state.isLoading) {
            LoadingAnimation()
        }
    }

    NetworkDialog(context = context, networkState = networkState)
}

@Composable
private fun MessageProfileItem(
    title: String,
    buttonText: String,
    imageUrl: String,
    contentDescription: String,
    onClicked: (() -> Unit)? = null,
) {
    Column {
        Text(
            text = title,
            style = StaticTypeScale.Default.body4,
            modifier = Modifier.padding(horizontal = 30.dp),
        )

        WSButton(
            onClick = {
                onClicked?.invoke()
            },
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

                if (onClicked != null) {
                    Icon(
                        painter = painterResource(id = R.drawable.edit),
                        contentDescription = stringResource(R.string.edit_icon),
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageContentItem(
    title: String,
    buttonText: String,
    onClick: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column {
        Text(
            text = title,
            style = StaticTypeScale.Default.body4,
            modifier = Modifier.padding(horizontal = 30.dp),
        )

        WSButton(
            onClick = onClick,
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
