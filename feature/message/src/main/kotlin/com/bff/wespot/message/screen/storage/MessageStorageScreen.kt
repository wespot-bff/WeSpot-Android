package com.bff.wespot.message.screen.storage

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.bff.wespot.designsystem.component.list.WSMessageItem
import com.bff.wespot.designsystem.component.list.WSMessageItemType
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.designsystem.theme.Primary400
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.message.R
import com.bff.wespot.message.common.RECEIVED_MESSAGE_INDEX
import com.bff.wespot.message.common.SENT_MESSAGE_INDEX
import com.bff.wespot.message.common.toStringWithDotSeparator
import com.bff.wespot.message.component.ReservedMessageBanner
import com.bff.wespot.message.model.MessageOptionType
import com.bff.wespot.message.state.storage.StorageAction
import com.bff.wespot.message.state.storage.StorageSideEffect
import com.bff.wespot.message.viewmodel.StorageViewModel
import com.bff.wespot.model.ToastState
import com.bff.wespot.model.message.request.MessageType
import com.bff.wespot.model.message.response.BaseMessage
import com.bff.wespot.model.message.response.Message
import com.bff.wespot.model.message.response.MessageStatus
import com.bff.wespot.model.message.response.ReceivedMessage
import com.bff.wespot.model.notification.NotificationType
import com.bff.wespot.ui.LoadingAnimation
import com.bff.wespot.ui.NetworkDialog
import com.bff.wespot.ui.SideEffectHandler
import com.bff.wespot.ui.WSBottomSheet
import com.bff.wespot.ui.WSHomeChipGroup
import com.bff.wespot.util.collectSideEffectAsState
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageStorageScreen(
    type: NotificationType,
    messageId: Int,
    navigateToReservedMessageScreen: () -> Unit,
    showToast: (ToastState) -> Unit,
    viewModel: StorageViewModel = hiltViewModel(),
) {
    val chipList = persistentListOf(
        stringResource(R.string.received_message),
        stringResource(R.string.reserved_message),
    )
    var selectedChipIndex by remember { mutableIntStateOf(0) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var showMessageDialog by remember { mutableStateOf(false) }
    var showMessageOptionDialog by remember { mutableStateOf(false) }

    val networkState by viewModel.networkState.collectAsStateWithLifecycle()
    val sideEffectState by viewModel.collectSideEffectAsState()
    val context = LocalContext.current

    val state by viewModel.collectAsState()
    val action = viewModel::onAction
    viewModel.collectSideEffect {
        when (it) {
            is StorageSideEffect.ShowToast -> {
                showToast(it.toastState)
            }

            is StorageSideEffect.ShowMessageDialog -> {
                showMessageDialog = true
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        WSHomeChipGroup(
            items = chipList,
            selectedItemIndex = selectedChipIndex,
            onSelectedChanged = { index -> selectedChipIndex = index },
        )

        Crossfade(
            targetState = selectedChipIndex,
            label = stringResource(R.string.message_storage_screen_crossfade),
        ) { page ->
            when (page) {
                RECEIVED_MESSAGE_INDEX -> {
                    val receivedMessageList = state.receivedMessageList.collectAsLazyPagingItems()
                    ReceivedMessageStorageScreen(
                        data = receivedMessageList,
                        itemClick = { item ->
                            action(StorageAction.OnReceivedMessageClicked(message = item))
                            showMessageDialog = true
                        },
                        optionButtonClick = { messageId ->
                            action(
                                StorageAction.OnOptionButtonClicked(
                                    messageId = messageId,
                                    messageType = MessageType.RECEIVED,
                                ),
                            )
                            showBottomSheet = true
                        },
                    )
                }

                SENT_MESSAGE_INDEX -> {
                    val sentMessageList = state.sentMessageList.collectAsLazyPagingItems()
                    SentMessageStorageScreen(
                        data = sentMessageList,
                        messageStatus = state.messageStatus,
                        isBannerVisible = state.messageStatus.hasReservedMessages() &&
                            state.isTimePeriodEveningToNight,
                        itemClick = { item ->
                            action(StorageAction.OnSentMessageClicked(message = item))
                            showMessageDialog = true
                        },
                        optionButtonClick = { messageId ->
                            action(
                                StorageAction.OnOptionButtonClicked(
                                    messageId = messageId,
                                    messageType = MessageType.SENT,
                                ),
                            )
                            // 보낸 쪽지인 경우, 삭제 옵션만 제공한다.
                            action(
                                StorageAction.OnOptionBottomSheetClicked(
                                    MessageOptionType.DELETE,
                                ),
                            )
                            showMessageOptionDialog = true
                        },
                        bannerClick = navigateToReservedMessageScreen,
                    )
                }
            }
        }
    }

    if (showBottomSheet) {
        WSBottomSheet(
            closeSheet = { showBottomSheet = false },
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 32.dp),
            ) {
                BottomSheetText(
                    text = stringResource(R.string.delete),
                    onClick = {
                        action(StorageAction.OnOptionBottomSheetClicked(MessageOptionType.DELETE))
                        showMessageOptionDialog = true
                    },
                )

                BottomSheetText(
                    text = stringResource(R.string.report_title),
                    onClick = {
                        action(StorageAction.OnOptionBottomSheetClicked(MessageOptionType.REPORT))
                        showMessageOptionDialog = true
                    },
                )

                BottomSheetText(
                    text = stringResource(R.string.block),
                    showDivider = false,
                    onClick = {
                        action(StorageAction.OnOptionBottomSheetClicked(MessageOptionType.BLOCK))
                        showMessageOptionDialog = true
                    },
                )
            }
        }
    }

    if (showMessageDialog) {
        MessageContentDialog(
            message = state.clickedMessage,
            closeButtonClick = { showMessageDialog = false },
        )
    }

    if (showMessageOptionDialog) {
        WSDialog(
            title = state.messageOptionType.title,
            subTitle = state.messageOptionType.subTitle,
            okButtonText = state.messageOptionType.okButtonText,
            cancelButtonText = state.messageOptionType.cancelButtonText,
            okButtonClick = {
                when (state.messageOptionType) {
                    MessageOptionType.DELETE -> {
                        action(
                            StorageAction.OnMessageDeleteButtonClicked,
                        )
                    }
                    MessageOptionType.BLOCK -> {
                        action(
                            StorageAction.OnMessageBlockButtonClicked,
                        )
                    }
                    MessageOptionType.REPORT -> {
                        action(
                            StorageAction.OnMessageReportButtonClicked,
                        )
                    }
                }
                showMessageOptionDialog = false
                showBottomSheet = false
            },
            onDismissRequest = { showMessageOptionDialog = false },
            cancelButtonClick = { showMessageOptionDialog = false },
        )
    }

    if (state.isLoading) {
        LoadingAnimation()
    }

    SideEffectHandler(effect = sideEffectState)

    NetworkDialog(context = context, networkState = networkState)

    LifecycleStartEffect(Unit) {
        action(StorageAction.StartTimeTracking)
        onStopOrDispose {
            action(StorageAction.CancelTimeTracking)
        }
    }

    LaunchedEffect(Unit) {
        when (type) {
            NotificationType.MESSAGE_RECEIVED -> {
                selectedChipIndex = RECEIVED_MESSAGE_INDEX
                action(
                    StorageAction.OnMessageStorageScreenOpened(
                        messageId = messageId,
                        type = MessageType.RECEIVED,
                    ),
                )
            }

            NotificationType.MESSAGE_SENT -> {
                selectedChipIndex = SENT_MESSAGE_INDEX
                action(
                    StorageAction.OnMessageStorageScreenOpened(
                        messageId = messageId,
                        type = MessageType.SENT,
                    ),
                )
            }

            else -> { }
        }
    }

    LaunchedEffect(selectedChipIndex) {
        when (selectedChipIndex) {
            RECEIVED_MESSAGE_INDEX -> {
                action(StorageAction.OnStorageChipSelected(MessageType.RECEIVED))
            }

            SENT_MESSAGE_INDEX -> {
                action(StorageAction.OnStorageChipSelected(MessageType.SENT))
            }
        }
    }
}

@Composable
private fun ReceivedMessageStorageScreen(
    data: LazyPagingItems<ReceivedMessage>,
    itemClick: (ReceivedMessage) -> Unit,
    optionButtonClick: (Int) -> Unit,
) {
    when (data.loadState.refresh) {
        is LoadState.Error -> {
        }

        is LoadState.Loading -> {
            LoadingAnimation()
        }

        else -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    data.itemCount,
                    key = data.itemKey { it.id },
                ) { index ->
                    val item = data[index]
                    item?.let {
                        WSMessageItem(
                            userInfo = item.senderName,
                            date = item.receivedAt?.toStringWithDotSeparator() ?: "",
                            wsMessageItemType = if (item.isRead) {
                                WSMessageItemType.ReadReceivedMessage
                            } else {
                                WSMessageItemType.UnreadReceivedMessage
                            },
                            itemClick = {
                                itemClick(it)
                            },
                            optionButtonClick = {
                                optionButtonClick(it.id)
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SentMessageStorageScreen(
    data: LazyPagingItems<Message>,
    messageStatus: MessageStatus,
    isBannerVisible: Boolean,
    itemClick: (Message) -> Unit,
    optionButtonClick: (Int) -> Unit,
    bannerClick: () -> Unit,
) {
    when (data.loadState.refresh) {
        is LoadState.Error -> {
        }

        is LoadState.Loading -> {
            LoadingAnimation()
        }

        else -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                if (isBannerVisible) {
                    item(span = { GridItemSpan(2) }) {
                        SentMessageStorageBanner(
                            messageStatus = messageStatus,
                            bannerClick = bannerClick,
                        )
                    }
                }

                items(
                    data.itemCount,
                    key = data.itemKey { it.id },
                ) { index ->
                    val item = data[index]

                    item?.let {
                        WSMessageItem(
                            userInfo = if (item.isBlocked.not() && item.isReported.not()) {
                                item.receiver.toUserInfoWithoutSchoolName()
                            } else {
                                null
                            },
                            schoolName = item.receiver.toShortSchoolName(),
                            date = item.receivedAt?.toStringWithDotSeparator() ?: "",
                            wsMessageItemType = when {
                                item.isBlocked -> WSMessageItemType.BlockedMessage
                                item.isReported -> WSMessageItemType.ReportedMessage
                                item.isRead -> WSMessageItemType.ReadSentMessage
                                else -> WSMessageItemType.UnreadSentMessage
                            },
                            itemClick = {
                                itemClick(it)
                            },
                            optionButtonClick = {
                                optionButtonClick(it.id)
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SentMessageStorageBanner(
    messageStatus: MessageStatus,
    bannerClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        ReservedMessageBanner(
            paddingValues = PaddingValues(),
            messageStatus = messageStatus,
            onBannerClick = bannerClick,
        )

        Text(
            modifier = Modifier.padding(top = 24.dp, start = 4.dp),
            text = stringResource(
                R.string.sent_message_storage_title,
            ),
            color = WeSpotThemeManager.colors.txtTitleColor,
            style = StaticTypeScale.Default.body3,
        )
    }
}

@Composable
private fun MessageContentDialog(
    message: BaseMessage,
    closeButtonClick: () -> Unit,
) {
    Dialog(onDismissRequest = { }) {
        Box(modifier = Modifier.width(296.dp)) {
            Image(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp)
                    .clickable { closeButtonClick() }
                    .zIndex(1f),
                painter = painterResource(id = R.drawable.close),
                contentDescription = stringResource(id = R.string.close),
            )

            Column(
                modifier = Modifier
                    .clip(WeSpotThemeManager.shapes.extraLarge)
                    .background(WeSpotThemeManager.colors.modalColor)
                    .border(
                        width = 1.dp,
                        color = Primary400,
                        shape = WeSpotThemeManager.shapes.extraLarge,
                    )
                    .padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    MessageDialogText("To.\n" + message.receiver.toDescription())

                    MessageDialogText(message.content, isMessageContent = true)
                }

                MessageDialogText(
                    text = "From.\n" + message.senderName,
                    textAlign = TextAlign.End,
                )
            }
        }
    }
}

@Composable
private fun MessageDialogText(
    text: String,
    textAlign: TextAlign = TextAlign.Start,
    isMessageContent: Boolean = false,
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .let {
                if (isMessageContent) it.heightIn(min = 192.dp, max = 240.dp) else it
            },
        text = text,
        style = StaticTypeScale.Default.body4,
        color = WeSpotThemeManager.colors.txtTitleColor,
        textAlign = textAlign,
    )
}

@Composable
private fun BottomSheetText(
    text: String,
    showDivider: Boolean = true,
    onClick: () -> Unit,
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        text = text,
        style = StaticTypeScale.Default.body4,
        color = Color(0xFFF7F7F8),
        textAlign = TextAlign.Center,
    )

    if (showDivider) {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color(0xFF4F5157),
        )
    }
}
