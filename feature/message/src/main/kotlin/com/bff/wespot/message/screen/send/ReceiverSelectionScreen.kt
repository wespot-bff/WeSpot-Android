package com.bff.wespot.message.screen.send

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.input.WsTextField
import com.bff.wespot.designsystem.component.input.WsTextFieldType
import com.bff.wespot.designsystem.theme.Gray300
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.message.R
import com.bff.wespot.message.component.SendExitDialog
import com.bff.wespot.message.state.send.SendAction
import com.bff.wespot.message.state.send.SendSideEffect
import com.bff.wespot.message.viewmodel.SendViewModel
import com.bff.wespot.model.common.KakaoContent
import com.bff.wespot.model.user.response.User
import com.bff.wespot.navigation.Navigator
import com.bff.wespot.ui.component.BottomButtonLayout
import com.bff.wespot.ui.component.NetworkDialog
import com.bff.wespot.ui.component.WSListItem
import com.bff.wespot.ui.util.handleSideEffect
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

interface ReceiverSelectionNavigator {
    fun navigateUp()
    fun navigateMessageWriteScreen(args: MessageWriteScreenArgs)
    fun popUpToMessageScreen()
}

data class ReceiverSelectionScreenArgs(
    val isEditing: Boolean,
)

@Destination(navArgsDelegate = ReceiverSelectionScreenArgs::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiverSelectionScreen(
    activityNavigator: Navigator,
    navigator: ReceiverSelectionNavigator,
    navArgs: ReceiverSelectionScreenArgs,
    viewModel: SendViewModel,
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current
    var dialogState by remember { mutableStateOf(false) }

    val state by viewModel.collectAsState()
    val pagingData = state.userList.collectAsLazyPagingItems()
    val action = viewModel::onAction

    val networkState by viewModel.networkState.collectAsStateWithLifecycle()

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

            else -> { }
        }
    }

    Scaffold(
        topBar = {
            WSTopBar(
                title = "",
                canNavigateBack = navArgs.isEditing,
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
                )
                .padding(it),
            showGradient = true,
            button = {
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
                    text = if (navArgs.isEditing) {
                        stringResource(R.string.edit_done)
                    } else {
                        stringResource(R.string.next)
                    },
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
                        action(SendAction.OnSearchContentChanged(value))
                    },
                    placeholder = stringResource(R.string.receiver_search_text_field_placeholder),
                    textFieldType = WsTextFieldType.Search,
                    focusRequester = focusRequester,
                    singleLine = true,
                )

                if (
                    pagingData.itemCount == 0 &&
                    state.isInputInitialized &&
                    state.selectedUser.isInitialized().not()
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
                                }
                                .clickable {
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
                    if (state.selectedUser.isInitialized() && state.isSelectedContext.not()) {
                        item {
                            ReceiverItem(
                                receiver = state.selectedUser,
                                selected = true,
                                onClick = {
                                    keyboard?.hide()
                                    action(SendAction.OnUserSelected(state.selectedUser))
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
                            if (item.id != state.selectedUser.id || state.isSelectedContext) {
                                ReceiverItem(
                                    receiver = item,
                                    selected = state.selectedUser.id == item.id,
                                    onClick = {
                                        keyboard?.hide()
                                        action(SendAction.OnUserSelected(item))
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
            isReservedMessage = state.isReservedMessage,
            okButtonClick = {
                action(SendAction.OnExitDialogExitButtonClicked)
            },
            cancelButtonClick = {
                action(SendAction.OnExitDialogCancelButtonClicked)
            },
        )
    }

    NetworkDialog(context = context, networkState = networkState)

    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
        delay(10)
        keyboard?.show()
    }

    LaunchedEffect(Unit) {
        action(SendAction.OnReceiverScreenEntered)
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
            AsyncImage(
                modifier = Modifier.size(46.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(receiver.profileCharacter.iconUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(
                    com.bff.wespot.ui.R.string.user_character_image,
                ),
                contentScale = ContentScale.Crop,
            )
        },
    )
}
