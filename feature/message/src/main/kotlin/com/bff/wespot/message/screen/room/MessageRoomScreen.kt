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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.designsystem.theme.Primary400
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.message.R
import com.bff.wespot.message.common.toStringWithDotSeparator
import com.bff.wespot.message.model.MessageCardType
import com.bff.wespot.message.state.room.RoomAction
import com.bff.wespot.message.state.room.RoomSideEffect
import com.bff.wespot.message.viewmodel.MessageRoomViewModel
import com.bff.wespot.model.message.response.MessageDetail
import com.bff.wespot.model.message.response.MessageRoom
import com.bff.wespot.ui.component.ProfileCircleImage
import com.bff.wespot.ui.util.clickableSingle
import com.ramcosta.composedestinations.annotation.Destination
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

interface MessageRoomNavigator {
    fun navigateUp()
    fun navigateMessageWriteScreen()
}

data class MessageRoomScreenArgs(
    val receiverId: Int,
)

@Destination(navArgsDelegate = MessageRoomScreenArgs::class)
@Composable
internal fun MessageRoomScreen(
    viewModel: MessageRoomViewModel = hiltViewModel(),
    navigator: MessageRoomNavigator,
) {
    var showDeleteConfirmModal by remember { mutableStateOf(false) }

    val state = viewModel.collectAsState().value
    val action = viewModel::onAction

    viewModel.collectSideEffect {
        when (it) {
            RoomSideEffect.NavigateToMessageWriteScreen -> {
                navigator.navigateMessageWriteScreen()
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
        if (state.messageRoom.messageDetails.isEmpty()) {
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(vertical = 24.dp),
        ) {
            MessageCard(
                type = if (state.selectedMessageDetail.isSend) MessageCardType.SENT else MessageCardType.RECEIVED,
                content = state.selectedMessageDetail.content,
                isLastItem = state.messageRoom.isLastMessage(state.selectedMessageDetail),
                onButtonClicked = {
                    action(RoomAction.OnReplyButtonClicked)
                },
                onRemoveButtonClicked = {
                    action(RoomAction.OnDeleteButtonClicked)
                },
            )

            Spacer(modifier = Modifier.weight(1f))

            MessageHorizontalList(
                messageRoom = state.messageRoom,
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
                action(RoomAction.OnDeleteConfirmed)
            },
            onDismissRequest = { },
            cancelButtonClick = {
                action(RoomAction.OnClosedModalButtonClicked)
            },
        )
    }
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
    content: String,
    isLastItem: Boolean,
    onButtonClicked: () -> Unit,
    onRemoveButtonClicked: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val image = type.backgroundImage

    Box(
        modifier = Modifier
            .padding(horizontal = 24.dp),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clickableSingle {
                    onRemoveButtonClicked()
                }
                .padding(top = 18.dp, end = 18.dp)
                .size(40.dp)
                .zIndex(99f),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.close),
                contentDescription = stringResource(id = R.string.close),
            )
        }

        Column(
            modifier = Modifier
                .height(height = 464.dp)
                .fillMaxWidth()
                .drawBehind {
                    val heightPx = 464.dp
                        .toPx()
                        .toInt()
                    val widthPx = size.width.toInt()
                    drawImage(image = image, dstSize = IntSize(widthPx, heightPx))
                }
                .padding(horizontal = 44.dp),
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 44.dp)
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
                    .verticalScroll(scrollState),
                text = content,
                style = StaticTypeScale.Default.body4,
                color = WeSpotThemeManager.colors.backgroundColor,
            )

            Spacer(modifier = Modifier.weight(1f))

            if (isLastItem) {
                WSButton(
                    text = type.buttonText,
                    enabled = type.buttonEnabled,
                    onClick = onButtonClicked,
                    paddingValues = PaddingValues(top = 36.dp, bottom = 42.dp),
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
    onItemClicked: (MessageDetail) -> Unit,
) {
    LazyRow(
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
                    .size(width = 80.dp, height = 90.dp)
                    .background(
                        color = WeSpotThemeManager.colors.cardBackgroundColor,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .clickableSingle {
                        onItemClicked(data)
                    }
                    .then(
                        if (data == selectedItem) {
                            Modifier.alpha(0.5f)
                        } else {
                            Modifier.border(
                                width = 1.dp,
                                color = Primary400,
                                shape = RoundedCornerShape(10.dp),
                            )
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
                                R.drawable.receive
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

@Preview
@Composable
private fun PreviewRoomScreen() {
    WeSpotTheme {
        Column {
            MessageRoomTopBar(
                MessageRoom(name = "jaino", isBookmarked = true),
                { },
            )

            MessageCard(
                type = MessageCardType.SENT,
                content = "dassasddasdasdassdasdsasasd",
                isLastItem = true,
                onButtonClicked = { },
                onRemoveButtonClicked = { },
            )

            Spacer(modifier = Modifier.height(20.dp))

            MessageHorizontalList(
                messageRoom = MessageRoom(
                    messageDetails = listOf(
                        MessageDetail(),
                    ),
                ),
                selectedItem = MessageDetail(),
            ) { }
        }
    }
}
