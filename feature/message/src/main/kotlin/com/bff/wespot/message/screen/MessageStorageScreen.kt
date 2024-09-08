package com.bff.wespot.message.screen

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
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
import com.bff.wespot.message.model.ClickedMessageUiModel
import com.bff.wespot.message.model.MessageOptionType
import com.bff.wespot.message.model.TimePeriod
import com.bff.wespot.message.state.MessageAction
import com.bff.wespot.message.state.MessageSideEffect
import com.bff.wespot.message.viewmodel.MessageViewModel
import com.bff.wespot.model.ToastState
import com.bff.wespot.model.message.request.MessageType
import com.bff.wespot.model.notification.NotificationType
import com.bff.wespot.ui.LoadingAnimation
import com.bff.wespot.ui.NetworkDialog
import com.bff.wespot.ui.WSBottomSheet
import com.bff.wespot.ui.WSHomeChipGroup
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageStorageScreen(
    viewModel: MessageViewModel,
    type: NotificationType,
    messageId: Int,
    navigateToReservedMessageScreen: () -> Unit,
    showToast: (ToastState) -> Unit,
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
    val context = LocalContext.current

    val state by viewModel.collectAsState()
    val action = viewModel::onAction
    viewModel.collectSideEffect {
        when (it) {
            is MessageSideEffect.ShowToast -> {
                showToast(it.toastState)
            }

            is MessageSideEffect.ShowMessageDialog -> {
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
                    val pagingData = state.receivedMessageList.collectAsLazyPagingItems()

                    when (pagingData.loadState.refresh) {
                        is LoadState.Error -> {
                            // TODO: Handle error
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
                                    pagingData.itemCount,
                                    key = pagingData.itemKey { it.id },
                                ) { index ->
                                    val item = pagingData[index]
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
                                                action(
                                                    MessageAction.OnReceivedMessageClicked(
                                                        message = item,
                                                    ),
                                                )
                                                showMessageDialog = true
                                            },
                                            optionButtonClick = {
                                                action(
                                                    MessageAction.OnOptionButtonClicked(
                                                        messageId = item.id,
                                                        messageType = MessageType.RECEIVED,
                                                    ),
                                                )
                                                showBottomSheet = true
                                            },
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                SENT_MESSAGE_INDEX -> {
                    val pagingData = state.sentMessageList.collectAsLazyPagingItems()

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        val hasReservedMessages = state.messageStatus.hasReservedMessages() &&
                            state.messageStatus.countRemainingMessages >= 0
                        val isEveningToNight = state.timePeriod == TimePeriod.EVENING_TO_NIGHT
                        val isBannerVisible = hasReservedMessages && isEveningToNight

                        if (isBannerVisible) {
                            item(span = { GridItemSpan(2) }) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    ReservedMessageBanner(
                                        paddingValues = PaddingValues(),
                                        messageStatus = state.messageStatus,
                                        onBannerClick = {
                                            navigateToReservedMessageScreen()
                                        },
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
                        }

                        items(
                            pagingData.itemCount,
                            key = pagingData.itemKey { it.id },
                        ) { index ->
                            val item = pagingData[index]

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
                                        action(
                                            MessageAction.OnSentMessageClicked(message = item),
                                        )
                                        showMessageDialog = true
                                    },
                                    optionButtonClick = {
                                        action(
                                            MessageAction.OnOptionButtonClicked(
                                                messageId = item.id,
                                                messageType = MessageType.SENT,
                                            ),
                                        )
                                        action(
                                            MessageAction.OnOptionBottomSheetClicked(
                                                MessageOptionType.DELETE,
                                            ),
                                        )
                                        showMessageOptionDialog = true
                                    },
                                )
                            }
                        }
                    }
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
                        action(MessageAction.OnOptionBottomSheetClicked(MessageOptionType.DELETE))
                        showMessageOptionDialog = true
                    },
                )

                BottomSheetText(
                    text = stringResource(R.string.report_title),
                    onClick = {
                        action(MessageAction.OnOptionBottomSheetClicked(MessageOptionType.REPORT))
                        showMessageOptionDialog = true
                    },
                )

                BottomSheetText(
                    text = stringResource(R.string.block),
                    showDivider = false,
                    onClick = {
                        action(MessageAction.OnOptionBottomSheetClicked(MessageOptionType.BLOCK))
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
                            MessageAction.OnMessageDeleteButtonClicked,
                        )
                    }
                    MessageOptionType.BLOCK -> {
                        action(
                            MessageAction.OnMessageBlockButtonClicked,
                        )
                    }
                    MessageOptionType.REPORT -> {
                        action(
                            MessageAction.OnMessageReportButtonClicked,
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

    NetworkDialog(context = context, networkState = networkState)

    LaunchedEffect(Unit) {
        when (type) {
            NotificationType.MESSAGE_RECEIVED -> {
                selectedChipIndex = RECEIVED_MESSAGE_INDEX
                action(
                    MessageAction.OnMessageStorageScreenOpened(
                        messageId = messageId,
                        type = MessageType.RECEIVED,
                    ),
                )
            }

            NotificationType.MESSAGE_SENT -> {
                selectedChipIndex = SENT_MESSAGE_INDEX
                action(
                    MessageAction.OnMessageStorageScreenOpened(
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
                action(MessageAction.OnStorageChipSelected(MessageType.RECEIVED))
            }

            SENT_MESSAGE_INDEX -> {
                action(MessageAction.OnStorageChipSelected(MessageType.SENT))
            }
        }
    }
}

@Composable
private fun MessageContentDialog(
    message: ClickedMessageUiModel,
    closeButtonClick: () -> Unit,
) {
    Dialog(onDismissRequest = { }) {
        Box(
            modifier = Modifier
                .width(296.dp)
                .heightIn(min = 376.dp, max = 424.dp),
        ) {
            Column(
                modifier = Modifier
                    .clip(WeSpotThemeManager.shapes.extraLarge)
                    .background(WeSpotThemeManager.colors.modalColor)
                    .border(
                        width = 1.dp,
                        color = Primary400,
                        shape = WeSpotThemeManager.shapes.extraLarge,
                    )
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                MessageDialogText("To.\n" + message.receiver)

                MessageDialogText(message.content, Modifier.weight(1f))

                MessageDialogText(
                    text = "From.\n" + message.sender,
                    textAlign = TextAlign.End,
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, end = 8.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Image(
                    modifier = Modifier.clickable { closeButtonClick() },
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = stringResource(id = R.string.close),
                )
            }
        }
    }
}

@Composable
private fun MessageDialogText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
) {
    Text(
        modifier = modifier.fillMaxWidth(),
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
