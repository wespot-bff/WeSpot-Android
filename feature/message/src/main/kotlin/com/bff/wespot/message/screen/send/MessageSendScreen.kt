package com.bff.wespot.message.screen.send

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
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
import com.bff.wespot.message.common.toStringWithDotSeparator
import com.bff.wespot.message.component.SendExitDialog
import com.bff.wespot.message.state.send.SendAction
import com.bff.wespot.message.state.send.SendSideEffect
import com.bff.wespot.message.viewmodel.SendViewModel
import com.bff.wespot.model.message.response.AnonymousProfile
import com.bff.wespot.ui.component.BottomButtonLayout
import com.bff.wespot.ui.component.LetterCountIndicator
import com.bff.wespot.ui.component.LoadingAnimation
import com.bff.wespot.ui.component.NetworkDialog
import com.bff.wespot.ui.component.ProfileCircleImage
import com.bff.wespot.ui.component.WSBottomSheet
import com.bff.wespot.ui.model.ToastState
import com.bff.wespot.ui.util.clickableSingle
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
                MessageProfileEditItem(
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

                MessageContentEditItem(
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

                MessageProfileEditItem(
                    title = stringResource(R.string.sender),
                    buttonText = if (state.isAnonymous) {
                        state.selectedAnonymousProfile.name
                    } else {
                        state.profile.toMessageReceiverInfo()
                    },
                    imageUrl = if (state.isAnonymous) {
                        state.selectedAnonymousProfile.imageUrl
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
                        action(SendAction.OnAnonymousToggled(it))
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

        if (state.showAnonymousProfileBottomSheet) {
            AnonymousProfileBottomSheet(
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

        if (state.showAnonymousProfileCreatorModal) {
            AnonymousProfileCreatorModal(
                imageUrl = state.anonymousProfileInput.imageUrl,
                name = state.anonymousProfileInput.name,
                hasProfanity = state.hasProfileNameProfanity,
                onNameChanged = { value ->
                    action(SendAction.OnProfileNameChanged(value))
                },
                onButtonClicked = { value ->
                    action(SendAction.OnProfileSelected(value))
                },
                onImageClicked = {
                    action(SendAction.OnProfileImageClicked)
                },
                onCloseButtonClicked = {
                    action(SendAction.OnProfileCreatorModalClosed)
                },
            )
        }

        if (state.showProfileOptionSheet) {
            ProfileOptionBottomSheet(
                closeSheet = { action(SendAction.OnProfileOptionSheetClosed) },
                onPickerOpenOptionClicked = { action(SendAction.OnPickerOpenOptionClicked) },
                onRemoveProfileOptionClicked = { action(SendAction.OnRemoveProfileOptionClicked) },
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
private fun MessageProfileEditItem(
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
private fun MessageContentEditItem(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnonymousProfileBottomSheet(
    anonymousProfileList: List<AnonymousProfile>,
    closeSheet: () -> Unit,
    onAnonymousProfileAddButtonClicked: () -> Unit,
    onAnonymousProfileSelected: (AnonymousProfile) -> Unit,
) {
    WSBottomSheet(closeSheet = closeSheet) {
        Column(
            modifier = Modifier.padding(start = 28.dp, end = 28.dp, top = 32.dp, bottom = 40.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                Text(
                    text = stringResource(R.string.anonymous_profile_sheet_title),
                    style = StaticTypeScale.Default.body1,
                    color = WeSpotThemeManager.colors.txtTitleColor,
                )

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = stringResource(R.string.anonymous_profile_sheet_content),
                    style = StaticTypeScale.Default.body6,
                    color = WeSpotThemeManager.colors.txtSubColor,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            anonymousProfileList.forEach {
                AnonymousProfileSheetItem(
                    profile = it,
                    onClick = { onAnonymousProfileSelected(it) },
                )
            }

            if (anonymousProfileList.size < 3) {
                AnonymousProfileAddSheetItem {
                    onAnonymousProfileAddButtonClicked()
                }
            }
        }
    }
}

@Composable
private fun AnonymousProfileSheetItem(
    profile: AnonymousProfile,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier.clickableSingle(removeInteraction = true, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProfileCircleImage(
            size = 34.dp,
            imageUrl = profile.imageUrl,
            contentDescription = stringResource(R.string.anonymous_profile_icon),
        )

        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            text = profile.name,
            style = StaticTypeScale.Default.body3,
            color = WeSpotThemeManager.colors.txtTitleColor,
        )

        Text(
            modifier = Modifier
                .padding(start = 12.dp),
            text = "최근 " + profile.lastSentDate.toStringWithDotSeparator(),
            style = StaticTypeScale.Default.body9,
            color = WeSpotThemeManager.colors.disableIcnColor,
        )

        Icon(
            modifier = Modifier.padding(start = 10.dp),
            painter = painterResource(id = com.bff.wespot.designsystem.R.drawable.right_arrow),
            contentDescription = stringResource(id = com.bff.wespot.designsystem.R.string.right_arrow),
        )
    }
}

@Composable
private fun AnonymousProfileAddSheetItem(
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier.clickableSingle(removeInteraction = true, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Image(
            modifier = Modifier.size(34.dp),
            painter = painterResource(id = R.drawable.add),
            contentDescription = stringResource(R.string.add_anonymous_profile_icon),
        )

        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.create_anonymous_profile_button_title),
            style = StaticTypeScale.Default.body3,
            color = WeSpotThemeManager.colors.txtTitleColor,
        )
    }
}

@Composable
private fun AnonymousProfileCreatorModal(
    imageUrl: String,
    name: String,
    hasProfanity: Boolean,
    onImageClicked: () -> Unit,
    onNameChanged: (String) -> Unit,
    onButtonClicked: (AnonymousProfile) -> Unit,
    onCloseButtonClicked: () -> Unit,
) {
    Dialog(
        onDismissRequest = { },
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(WeSpotThemeManager.colors.modalColor)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier.align(Alignment.End),
            ) {
                Icon(
                    modifier = Modifier.clickableSingle(onClick = onCloseButtonClicked),
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = stringResource(R.string.close_anonymous_profile_creator_modal_button),
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                text = "받는 사람에게 보여질\n익명 프로필을 설정해 주세요",
                style = StaticTypeScale.Default.header1,
                color = WeSpotThemeManager.colors.txtTitleColor,
                textAlign = TextAlign.Center,
            )

            Box(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .clickableSingle(removeInteraction = true, onClick = onImageClicked),
            ) {
                ProfileCircleImage(
                    size = 87.dp,
                    imageUrl = imageUrl,
                    contentDescription = "Anonymous Profile Image",
                )

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.BottomEnd)
                        .clip(WeSpotThemeManager.shapes.small)
                        .background(WeSpotThemeManager.colors.secondaryBtnColor)
                        .zIndex(1f),
                ) {
                    Image(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(14.dp),
                        painter = painterResource(id = R.drawable.album),
                        contentDescription = stringResource(R.string.edit_icon),
                    )
                }
            }

            AnonymousProfileNameTextField(value = name, onValueChanged = onNameChanged)

            if (hasProfanity) {
                Text(
                    modifier = Modifier
                        .padding(top = 4.dp, start = 12.dp)
                        .fillMaxWidth(),
                    text = stringResource(com.bff.wespot.designsystem.R.string.has_profanity),
                    style = StaticTypeScale.Default.body7,
                    color = WeSpotThemeManager.colors.dangerColor,
                )
            }

            WSButton(
                paddingValues = PaddingValues(top = 40.dp),
                text = "설정 완료",
                enabled = hasProfanity.not() && name.length in 1..10,
                onClick = {
                    onButtonClicked(
                        AnonymousProfile(name = name, imageUrl = imageUrl),
                    )
                },
                content = { it() },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnonymousProfileNameTextField(
    value: String,
    onValueChanged: (String) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 36.dp),
        value = value,
        onValueChange = onValueChanged,
        textStyle = StaticTypeScale.Default.body4.copy(
            color = WeSpotThemeManager.colors.txtTitleColor,
        ),
        cursorBrush = SolidColor(WeSpotThemeManager.colors.txtTitleColor),
        decorationBox = { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = value,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                placeholder = {
                    Text(
                        text = "닉네임을 입력해 주세요",
                        style = StaticTypeScale.Default.body4,
                        color = WeSpotThemeManager.colors.disableBtnColor,
                    )
                },
                trailingIcon = {
                    LetterCountIndicator(
                        currentCount = value.length,
                        maxCount = 10,
                        paddingValues = PaddingValues(),
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = WeSpotThemeManager.colors.modalColor,
                    unfocusedContainerColor = WeSpotThemeManager.colors.modalColor,
                    focusedIndicatorColor = WeSpotThemeManager.colors.disableBtnColor,
                    unfocusedIndicatorColor = WeSpotThemeManager.colors.disableBtnColor,
                    cursorColor = WeSpotThemeManager.colors.txtTitleColor,
                ),
                contentPadding = PaddingValues(0.dp),
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileOptionBottomSheet(
    closeSheet: () -> Unit,
    onPickerOpenOptionClicked: () -> Unit,
    onRemoveProfileOptionClicked: () -> Unit,
) {
    WSBottomSheet(
        closeSheet = closeSheet,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "앨범에서 사진 선택",
                modifier = Modifier
                    .clickableSingle(onClick = onPickerOpenOptionClicked)
                    .fillMaxWidth()
                    .padding(top = 28.dp, bottom = 16.dp, start = 28.dp, end = 28.dp)
                    .clip(RoundedCornerShape(8.dp)),
                textAlign = TextAlign.Center,
                style = StaticTypeScale.Default.body4,
            )

            HorizontalDivider(color = Color(0xFF4F5157))

            Text(
                text = "기본 이미지 적용",
                modifier = Modifier
                    .clickableSingle(onClick = onRemoveProfileOptionClicked)
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 48.dp, start = 28.dp, end = 28.dp)
                    .clip(RoundedCornerShape(8.dp)),
                textAlign = TextAlign.Center,
                style = StaticTypeScale.Default.body4,
            )
        }
    }
}
