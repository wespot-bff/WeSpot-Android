package com.bff.wespot.message.screen.room

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.bff.wespot.analytics.AnalyticsEvent
import com.bff.wespot.analytics.AnalyticsHelper
import com.bff.wespot.analytics.LocalAnalyticsHelper
import com.bff.wespot.analytics.TrackScreenViewEvent
import com.bff.wespot.analytics.logClick
import com.bff.wespot.analytics.logImpression
import com.bff.wespot.analytics.params.AreaParams
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.designsystem.theme.Primary400
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.message.R
import com.bff.wespot.message.common.toStringWithDotSeparator
import com.bff.wespot.message.model.MessageCardType
import com.bff.wespot.message.screen.send.MessageWriteScreenArgs
import com.bff.wespot.message.state.room.RoomAction
import com.bff.wespot.message.state.room.RoomSideEffect
import com.bff.wespot.message.viewmodel.MessageRoomViewModel
import com.bff.wespot.model.message.response.MessageDetail
import com.bff.wespot.model.message.response.MessageRoom
import com.bff.wespot.ui.component.ProfileCircleImage
import com.bff.wespot.ui.component.verticalScrollIndicator
import com.bff.wespot.ui.util.clickableSingle
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

interface MessageRoomNavigator {
    fun navigateUp()
    fun navigateMessageWriteScreen(args: MessageWriteScreenArgs)
}

data class MessageRoomScreenArgs(
    val roomId: Int,
)

@Destination(
    deepLinks = [
        DeepLink(
            uriPattern = "wespot://message/room/{roomId}",
        ),
    ],
    navArgsDelegate = MessageRoomScreenArgs::class,
)
@Composable
internal fun MessageRoomScreen(
    viewModel: MessageRoomViewModel = hiltViewModel(),
    navigator: MessageRoomNavigator,
) {
    val analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current
    var showDeleteConfirmModal by remember { mutableStateOf(false) }
    var showReplyNoticeModal by remember { mutableStateOf(false) }

    val state = viewModel.collectAsState().value
    val action = viewModel::onAction

    viewModel.collectSideEffect {
        when (it) {
            RoomSideEffect.NavigateToMessageWriteScreen -> {
                navigator.navigateMessageWriteScreen(
                    args = MessageWriteScreenArgs(
                        isReplyContext = true,
                        roomId = state.messageRoom.messageRoomId,
                        receiverName = state.messageRoom.name,
                    ),
                )
            }
            RoomSideEffect.NavigateUp -> {
                navigator.navigateUp()
            }
            RoomSideEffect.ShowMessageDeleteConfirmModal -> {
                showDeleteConfirmModal = true
            }
            RoomSideEffect.CloseMessageDeleteConfirmModal -> {
                showDeleteConfirmModal = false
            }
            RoomSideEffect.ShowReplyNoticeModal -> {
                showReplyNoticeModal = true
            }
            RoomSideEffect.CloseReplyNoticeModal -> {
                showReplyNoticeModal = false
            }
        }
    }

    Scaffold(
        topBar = {
            MessageRoomTopBar(
                data = state.messageRoom,
                navigateUp = {
                    action(RoomAction.OnTopBarNavigate)
                },
            )
        },
    ) { innerPadding ->
        if (state.selectedMessageDetail == null) {
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(vertical = 24.dp),
        ) {
            MessageCard(
                type = if (state.selectedMessageDetail.isSend) MessageCardType.SENT else MessageCardType.RECEIVED,
                detail = state.selectedMessageDetail,
                showReplyButton = state.messageRoom.isLastReceivedMessage(state.selectedMessageDetail),
                showDeleteButton = !state.messageRoom.isSingleMessage(),
                onReplyButtonClicked = {
                    analyticsHelper.logClick("reply_message")
                    action(RoomAction.OnReplyButtonClicked)
                },
                onDeleteButtonClicked = {
                    analyticsHelper.logClick("delete_message")
                    action(RoomAction.OnDeleteButtonClicked)
                },
            )

            Spacer(modifier = Modifier.weight(1f))

            MessageHorizontalList(
                messageRoom = state.messageRoom,
                analyticsHelper = analyticsHelper,
                onItemClicked = {
                    action(RoomAction.OnMessageDetailSelected(it))
                },
                selectedItem = state.selectedMessageDetail,
            )
        }
    }

    if (showDeleteConfirmModal) {
        WSDialog(
            title = stringResource(R.string.message_delete_dialog_title),
            subTitle = stringResource(R.string.message_delete_dialog_subtitle),
            okButtonText = stringResource(R.string.message_delete_dialog_ok_button),
            cancelButtonText = stringResource(id = R.string.close),
            okButtonClick = {
                analyticsHelper.logClick(
                    name = "delete_message",
                    area = AreaParams.MODAL,
                )
                action(RoomAction.OnDeleteConfirmed)
            },
            onDismissRequest = { },
            cancelButtonClick = {
                action(RoomAction.OnClosedModalButtonClicked)
            },
        )
    }

    if (showReplyNoticeModal) {
        WSDialog(
            title = stringResource(R.string.message_reply_notice_modal_title),
            subTitle = stringResource(R.string.message_reply_notice_modal_subtitle),
            okButtonText = stringResource(R.string.message_reply_notice_modal_ok_button),
            cancelButtonText = stringResource(id = R.string.cancel),
            okButtonClick = {
                action(RoomAction.OnNoticeModalOkButtonClicked)
            },
            cancelButtonClick = {
                action(RoomAction.OnNoticeModalCloseButtonClicked)
            },
            onDismissRequest = { },
        )
    }

    LaunchedEffect(Unit) {
        action(RoomAction.OnScreenEntered)
    }

    TrackScreenViewEvent("message_room")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MessageRoomTopBar(
    data: MessageRoom,
    navigateUp: () -> Unit,
) {
    WSTopBar(
        title = "",
        titleContent = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .clip(WeSpotThemeManager.shapes.extraLarge)
                        .background(WeSpotThemeManager.colors.bottomSheetColor)
                        .padding(horizontal = 8.dp, vertical = 1.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (data.isBookmarked) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.bookmark),
                                tint = WeSpotThemeManager.colors.primaryColor,
                                contentDescription = "Bookmarked User",
                            )
                        }

                        Text(
                            text = data.getReceiverStatus(),
                            color = WeSpotThemeManager.colors.txtTitleColor,
                            style = StaticTypeScale.Default.body9,
                        )
                    }
                }

                ProfileCircleImage(
                    size = 30.dp,
                    imageUrl = data.thumbnail,
                    contentDescription = stringResource(id = R.string.receiver_profile_image),
                )

                Text(
                    text = data.name,
                    style = StaticTypeScale.Default.header2,
                    color = WeSpotThemeManager.colors.txtTitleColor,
                )
            }
        },
        canNavigateBack = true,
        navigateUp = navigateUp,
    )
}

@Composable
private fun MessageCard(
    type: MessageCardType,
    detail: MessageDetail,
    showReplyButton: Boolean,
    showDeleteButton: Boolean,
    onReplyButtonClicked: () -> Unit,
    onDeleteButtonClicked: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val image = type.backgroundImage

    Box(
        modifier = Modifier
            .padding(horizontal = 24.dp),
    ) {
        if (showDeleteButton) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clickableSingle {
                        onDeleteButtonClicked()
                    }.padding(top = 18.dp, end = 18.dp)
                    .size(40.dp)
                    .zIndex(99f),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.close),
                    tint = WeSpotThemeManager.colors.secondaryBtnColor,
                    contentDescription = stringResource(id = R.string.close),
                )
            }
        }

        Column(
            modifier = Modifier
                .height(height = 464.dp)
                .fillMaxWidth()
                .drawBehind {
                    val heightPx = 464
                        .dp
                        .toPx()
                        .toInt()
                    val widthPx = size.width.toInt()
                    drawImage(image = image, dstSize = IntSize(widthPx, heightPx))
                }.padding(horizontal = 18.dp),
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 44.dp, start = 26.dp)
                    .background(
                        color = type.chipBackgroundColor,
                        shape = WeSpotThemeManager.shapes.extraLarge,
                    ),
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                    text = type.chipText,
                    style = StaticTypeScale.Default.badge,
                    color = type.chipTextColor,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier
                    .height(240.dp)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .verticalScrollIndicator(
                        scrollState = scrollState,
                        width = 4.dp,
                        paddingValues = PaddingValues(end = 4.dp),
                    ).padding(horizontal = 26.dp),
                text = detail.content,
                style = StaticTypeScale.Default.body4,
                color = WeSpotThemeManager.colors.backgroundColor,
            )

            Spacer(modifier = Modifier.weight(1f))

            if (showReplyButton) {
                WSButton(
                    text = type.buttonText,
                    enabled = detail.isAbleToAnswer,
                    onClick = onReplyButtonClicked,
                    paddingValues = PaddingValues(top = 36.dp, bottom = 42.dp, start = 16.dp, end = 16.dp),
                    content = { it() },
                )
            }
        }
    }
}

@Composable
private fun MessageHorizontalList(
    messageRoom: MessageRoom,
    selectedItem: MessageDetail,
    analyticsHelper: AnalyticsHelper,
    onItemClicked: (MessageDetail) -> Unit,
) {
    val listState = rememberLazyListState()
    var hasLoggedImpression by remember { mutableStateOf(false) }

    LaunchedEffect(messageRoom.messageDetails.size) {
        val selectedIndex = messageRoom.messageDetails.indexOf(selectedItem)
        if (selectedIndex >= 0) {
            listState.animateScrollToItem(selectedIndex)
            if (!hasLoggedImpression && messageRoom.messageDetails.isNotEmpty()) {
                logMessageListImpression(messageRoom, analyticsHelper)
                hasLoggedImpression = true
            }
        }
    }

    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        items(
            items = messageRoom.messageDetails,
            key = { it.id },
        ) { data ->
            Box(
                modifier = Modifier
                    .height(90.dp)
                    .background(
                        color = WeSpotThemeManager.colors.cardBackgroundColor,
                        shape = RoundedCornerShape(10.dp),
                    ).clickableSingle {
                        onItemClicked(data)
                    }.then(
                        if (data == selectedItem) {
                            Modifier.border(
                                width = 1.dp,
                                color = Primary400,
                                shape = RoundedCornerShape(10.dp),
                            )
                        } else {
                            Modifier.alpha(0.5f)
                        },
                    ),
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 14.dp, bottom = 10.dp, start = 14.dp, end = 14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(
                            id = if (data.isSend) {
                                R.drawable.sent
                            } else {
                                R.drawable.received
                            },
                        ),
                        contentDescription = stringResource(R.string.message_type_icon),
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Image(
                        modifier = Modifier.size(width = 32.dp, height = 21.dp),
                        painter = painterResource(id = R.drawable.message),
                        contentDescription = stringResource(id = R.string.message),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = data.createdAt?.toStringWithDotSeparator() ?: "",
                        style = StaticTypeScale.Default.body9,
                        color = WeSpotThemeManager.colors.txtTitleColor,
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

private fun logMessageListImpression(
    messageRoom: MessageRoom,
    analyticsHelper: AnalyticsHelper,
) {
    val messageDetails = messageRoom.messageDetails
    val firstSendTime = messageDetails
        .firstOrNull()
        ?.createdAt
        ?.toStringWithDotSeparator()
        .orEmpty()
    val lastSendTime = messageDetails
        .lastOrNull()
        ?.createdAt
        ?.toStringWithDotSeparator()
        .orEmpty()

    analyticsHelper.logImpression(
        name = "message_list",
        extras = buildList {
            add(AnalyticsEvent.Param("size", messageDetails.size.toString()))
            add(AnalyticsEvent.Param("first_send_time", firstSendTime))
            add(AnalyticsEvent.Param("last_send_time", lastSendTime))
        },
    )
}
