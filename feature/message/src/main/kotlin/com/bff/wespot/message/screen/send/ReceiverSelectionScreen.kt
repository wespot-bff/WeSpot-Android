package com.bff.wespot.message.screen.send

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.input.WsTextField
import com.bff.wespot.designsystem.component.input.WsTextFieldType
import com.bff.wespot.designsystem.theme.Gray300
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.message.R
import com.bff.wespot.message.component.ProfileSelectBottomSheet
import com.bff.wespot.message.component.SendExitDialog
import com.bff.wespot.message.model.AnonymousProfile
import com.bff.wespot.message.screen.room.MessageRoomScreenArgs
import com.bff.wespot.message.state.send.receiver.ReceiverAction
import com.bff.wespot.message.state.send.receiver.ReceiverSideEffect
import com.bff.wespot.message.viewmodel.SendViewModel
import com.bff.wespot.model.common.KakaoContent
import com.bff.wespot.model.user.response.User
import com.bff.wespot.navigation.Navigator
import com.bff.wespot.ui.component.BottomButtonLayout
import com.bff.wespot.ui.component.LoadingAnimation
import com.bff.wespot.ui.component.NetworkDialog
import com.bff.wespot.ui.component.ProfileCircleImage
import com.bff.wespot.ui.component.WSListItem
import com.bff.wespot.ui.model.ToastState
import com.bff.wespot.ui.util.handleSideEffect
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

interface ReceiverSelectionNavigator {
    fun navigateUp()
    fun navigateMessageWriteScreen()
    fun navigateToMessageRoomScreen(args: MessageRoomScreenArgs)
    fun popUpToMessageScreen()
}

@Destination
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiverSelectionScreen(
    activityNavigator: Navigator,
    navigator: ReceiverSelectionNavigator,
    viewModel: SendViewModel,
    showToast: (ToastState) -> Unit,
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current
    var dialogState by remember { mutableStateOf(false) }
    var showAnonymousProfileModal by remember { mutableStateOf(false) }
    var showProfileSelectBottomSheet by remember { mutableStateOf(false) }

    val state by viewModel.collectAsState()
    val pagingData = state.receiverList.collectAsLazyPagingItems()
    val action: (ReceiverAction) -> Unit = viewModel::onAction

    val networkState by viewModel.networkState.collectAsStateWithLifecycle()

    handleSideEffect(viewModel.sideEffect)

    viewModel.collectSideEffect {
        if (it is ReceiverSideEffect) {
            when (it) {
                ReceiverSideEffect.DismissExitDialog -> {
                    dialogState = false
                }
                ReceiverSideEffect.NavigateToMessage -> {
                    /** 키보드가 올라간 채로 화면 전환시, 화면이 일그러지는 것을 방지한다. */
                    keyboard?.hide()
                    navigator.popUpToMessageScreen()
                }
                ReceiverSideEffect.NavigateUp -> navigator.navigateUp()
                ReceiverSideEffect.NavigateToMessageWriteScreen -> {
                    keyboard?.hide()
                    navigator.navigateMessageWriteScreen()
                }
                ReceiverSideEffect.ShowAnonymousProfileModal -> {
                    showAnonymousProfileModal = true
                }
                ReceiverSideEffect.DismissAnonymousProfileModal -> {
                    showAnonymousProfileModal = false
                }
                ReceiverSideEffect.ShowProfileSelectBottomSheet -> {
                    showProfileSelectBottomSheet = true
                }
                ReceiverSideEffect.DismissProfileSelectBottomSheet -> {
                    showProfileSelectBottomSheet = false
                }
                is ReceiverSideEffect.NavigateToMessageRoomScreen -> {
                    navigator.navigateToMessageRoomScreen(
                        MessageRoomScreenArgs(it.roomId),
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
            WSTopBar(
                title = "",
                action = {
                    Text(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable {
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
        BottomButtonLayout(
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = interactionSource,
                    onClick = { keyboard?.hide() },
                ).padding(it),
            showGradient = true,
            button = {
                WSButton(
                    onClick = {
                        action(ReceiverAction.OnSelectDoneButtonClicked)
                    },
                    enabled = state.receiver.name.isNotBlank(),
                    text = stringResource(R.string.next),
                    content = { it() },
                )
            },
        ) {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 4.dp),
                    text = stringResource(R.string.receiver_screen_title, state.profile.name),
                    style = StaticTypeScale.Default.header1,
                    color = WeSpotThemeManager.colors.txtTitleColor,
                )

                Spacer(modifier = Modifier.height(16.dp))

                WsTextField(
                    value = state.nameInput,
                    onValueChange = { value ->
                        action(ReceiverAction.OnSearchContentChanged(value))
                    },
                    placeholder = stringResource(R.string.receiver_search_text_field_placeholder),
                    textFieldType = WsTextFieldType.Search,
                    focusRequester = focusRequester,
                    singleLine = true,
                )

                if (
                    pagingData.itemCount == 0 &&
                    state.isInputInitialized &&
                    state.receiver.isInitialized().not()
                ) {
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
                                }.clickable {
                                    if (state.kakaoContent != KakaoContent.EMPTY) {
                                        activityNavigator.navigateToKakao(
                                            context = context,
                                            title = state.kakaoContent.title,
                                            description = state.kakaoContent.description,
                                            imageUrl = state.kakaoContent.imageUrl,
                                            buttonText = state.kakaoContent.buttonText,
                                            url = state.kakaoContent.url,
                                        )
                                    }
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
                    /** 선택된 유저는 상위로 고정해야 하며, 처음 선택한 경우에는 고정하지 않는다. */
                    if (state.receiver.isInitialized() && state.isSelectedContext.not()) {
                        item {
                            ReceiverItem(
                                receiver = state.receiver,
                                selected = true,
                                onClick = {
                                    keyboard?.hide()
                                    action(ReceiverAction.OnUserSelected(state.receiver))
                                },
                            )
                        }
                    }

                    items(
                        pagingData.itemCount,
                        key = pagingData.itemKey { key -> key.id },
                    ) { index ->
                        val item = pagingData[index]

                        item?.let {
                            if (item.id != state.receiver.id || state.isSelectedContext) {
                                ReceiverItem(
                                    receiver = item,
                                    selected = state.receiver.id == item.id,
                                    onClick = {
                                        keyboard?.hide()
                                        action(ReceiverAction.OnUserSelected(item))
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (dialogState) {
        SendExitDialog(
            okButtonClick = {
                action(ReceiverAction.OnExitDialogExitButtonClicked)
            },
            cancelButtonClick = {
                action(ReceiverAction.OnExitDialogCancelButtonClicked)
            },
        )
    }

    if (showProfileSelectBottomSheet) {
        ProfileSelectBottomSheet(
            profileList = state.senderProfileList,
            closeSheet = {
                action(ReceiverAction.OnProfileBottomSheetClosed)
            },
            onProfileAddButtonClicked = {
                action(ReceiverAction.OnProfileAddButtonClicked)
            },
            onProfileSelected = {
                action(ReceiverAction.OnProfileBottomSheetSelected(it))
            },
            showToast = showToast,
        )
    }

    if (showAnonymousProfileModal) {
        AnonymousProfileModal(
            profile = AnonymousProfile(
                name = state.senderProfile.name,
                imageUrl = state.senderProfile.image,
            ),
            onProfileSelected = {
                action(ReceiverAction.OnAnonymousProfileSelected(it))
            },
            onDismiss = {
                action(ReceiverAction.OnAnonymousProfileModalDismiss)
            },
        )
    }

    if (state.isLoading) {
        LoadingAnimation()
    }

    NetworkDialog(context = context, networkState = networkState)

    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
        delay(10)
        keyboard?.show()
    }

    LaunchedEffect(Unit) {
        action(ReceiverAction.OnReceiverScreenEntered)
    }
}

@Composable
fun LazyItemScope.ReceiverItem(
    receiver: User,
    selected: Boolean,
    onClick: () -> Unit,
) {
    WSListItem(
        modifier = Modifier.animateItem(),
        title = receiver.name,
        subTitle = receiver.toSchoolInfo(),
        selected = selected,
        backgroundColor = receiver.profileCharacter.backgroundColor,
        onClick = onClick,
        imageContent = {
            ProfileCircleImage(
                size = 46.dp,
                imageUrl = receiver.profileCharacter.iconUrl,
                contentDescription = stringResource(
                    com.bff.wespot.ui.R.string.user_character_image,
                ),
            )
        },
    )
}
